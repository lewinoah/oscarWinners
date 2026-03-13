import { useState } from 'react'
import { MovieCard } from './MovieCard'
import './MovieList.css'

export function MovieList({ movies, apiBase }) {
  const [expandedId, setExpandedId] = useState(null)

  if (!movies.length) {
    return (
      <p className="movie-list-empty">
        No movies in the database. Run the backend and trigger a sync to fetch data from OMDb.
      </p>
    )
  }

  return (
    <ul className="movie-list">
      {movies.map((movie) => (
        <li key={movie.id}>
          <MovieCard
            movie={movie}
            expanded={expandedId === movie.id}
            onToggleExpand={() =>
              setExpandedId((id) => (id === movie.id ? null : movie.id))
            }
          />
        </li>
      ))}
    </ul>
  )
}
