package com.oscarwinners.repository;

import com.oscarwinners.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByOrderByYearAsc();

    Optional<Movie> findByImdbId(String imdbId);

    List<Movie> findByOmdbSyncedFalseOrOmdbSyncedIsNull();
}
