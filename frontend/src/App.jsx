import { useState, useEffect } from 'react'
import { MovieList } from './components/MovieList'
import { SyncButton } from './components/SyncButton'
import './App.css'

const API_URL = import.meta.env.VITE_API_URL || ''

function App() {
  const [movies, setMovies] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [year, setYear] = useState('')
  const [sortBy, setSortBy] = useState('year')
  const [direction, setDirection] = useState('asc')

  const fetchMovies = async (options = {}) => {
    const nextYear = options.year ?? year
    const nextSortBy = options.sortBy ?? sortBy
    const nextDirection = options.direction ?? direction

    const params = new URLSearchParams()
    if (nextYear) params.set('year', nextYear)
    if (nextSortBy) params.set('sortBy', nextSortBy)
    if (nextDirection) params.set('direction', nextDirection)

    setLoading(true)
    setError(null)
    try {
      const query = params.toString()
      const url = query
        ? `${API_URL}/api/movies?${query}`
        : `${API_URL}/api/movies`
      const res = await fetch(url)
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const data = await res.json()
      setMovies(Array.isArray(data) ? data : [])
    } catch (e) {
      setError(e.message || 'Failed to load movies')
      setMovies([])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchMovies()
  }, []) // initial load

  const handleYearChange = (value) => {
    setYear(value)
    fetchMovies({ year: value })
  }

  const handleSortChange = (value) => {
    setSortBy(value)
    fetchMovies({ sortBy: value })
  }

  const handleDirectionChange = (value) => {
    setDirection(value)
    fetchMovies({ direction: value })
  }

  const handleSyncDone = () => {
    fetchMovies()
  }

  return (
    <div className="app">
      <header className="header">
        <h1>Oscar Best Picture Winners</h1>
        <p className="subtitle">Academy Award winners for Best Picture</p>
        <SyncButton onSyncDone={handleSyncDone} apiBase={API_URL} />
      </header>

      <main className="main">
        <section className="controls-bar">
          <div className="controls-left">
            <label>
              Year:{' '}
              <select
                value={year}
                onChange={(e) => handleYearChange(e.target.value)}
              >
                <option value="">All years</option>
                {Array.from(
                  new Set(movies.map((m) => m.year).filter(Boolean))
                )
                  .sort((a, b) => b - a)
                  .map((y) => (
                    <option key={y} value={y}>
                      {y}
                    </option>
                  ))}
              </select>
            </label>

            <label>
              Sort by:{' '}
              <select
                value={sortBy}
                onChange={(e) => handleSortChange(e.target.value)}
              >
                <option value="year">Year</option>
                <option value="title">Title</option>
              </select>
            </label>

            <label>
              Direction:{' '}
              <select
                value={direction}
                onChange={(e) => handleDirectionChange(e.target.value)}
              >
                <option value="asc">Ascending</option>
                <option value="desc">Descending</option>
              </select>
            </label>
          </div>
        </section>

        {loading && <div className="state state-loading">Loading…</div>}
        {error && (
          <div className="state state-error" role="alert">
            Error: {error}. Make sure the backend is running on port 8080.
          </div>
        )}
        {!loading && !error && (
          <MovieList movies={movies} apiBase={API_URL} />
        )}
      </main>
    </div>
  )
}

export default App
