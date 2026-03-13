export function MovieCard({ movie, expanded, onToggleExpand }) {
  const hasPoster = movie.posterUrl && movie.posterUrl !== 'N/A'

  return (
    <article className="movie-card">
      <div className="movie-poster-wrapper">
        {hasPoster ? (
          <img
            src={movie.posterUrl}
            alt={`Poster for ${movie.title}`}
            className="movie-poster"
          />
        ) : (
          <div className="movie-poster placeholder">No poster</div>
        )}
      </div>
      <div className="movie-body">
        <h2>{movie.title}</h2>
        <div className="movie-year">{movie.year != null ? movie.year : '—'}</div>
        <div className="movie-meta">
          {movie.runtime && <span>{movie.runtime}</span>}
          {movie.rated && <span>Rated {movie.rated}</span>}
          {movie.released && <span>{movie.released}</span>}
          {movie.genre && <span>{movie.genre}</span>}
        </div>
        {movie.plot && <p className="movie-plot">{movie.plot}</p>}
        {movie.director && (
          <div className="movie-detail">
            <strong>Director:</strong> {movie.director}
          </div>
        )}
        {movie.actors && (
          <div className="movie-detail">
            <strong>Actors:</strong> {movie.actors}
          </div>
        )}
        {movie.awards && (
          <div className="movie-detail">
            <strong>Awards:</strong> {movie.awards}
          </div>
        )}
        {(movie.language || movie.country || movie.metascore) && (
          <>
            <button
              type="button"
              className="expand-btn"
              onClick={onToggleExpand}
              aria-expanded={expanded}
            >
              {expanded ? 'Show less' : 'Show more'}
            </button>
            {expanded && (
              <div className="movie-extra">
                {movie.language && (
                  <div className="movie-detail">
                    <strong>Language:</strong> {movie.language}
                  </div>
                )}
                {movie.country && (
                  <div className="movie-detail">
                    <strong>Country:</strong> {movie.country}
                  </div>
                )}
                {movie.metascore && (
                  <div className="movie-detail">
                    <strong>Metascore:</strong> {movie.metascore}
                  </div>
                )}
              </div>
            )}
          </>
        )}
      </div>
    </article>
  )
}
