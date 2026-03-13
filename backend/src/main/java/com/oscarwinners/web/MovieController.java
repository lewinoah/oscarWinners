package com.oscarwinners.web;

import com.oscarwinners.entity.Movie;
import com.oscarwinners.omdb.OmdbService;
import com.oscarwinners.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieRepository movieRepository;
    private final OmdbService omdbService;

    public MovieController(MovieRepository movieRepository, OmdbService omdbService) {
        this.movieRepository = movieRepository;
        this.omdbService = omdbService;
    }

    @GetMapping
    public List<Movie> list(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "year") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction) {
        List<Movie> movies = movieRepository.findAll();

        if (year != null) {
            movies = movies.stream()
                    .filter(m -> year.equals(m.getYear()))
                    .toList();
        }

        Comparator<Movie> comparator;
        if ("title".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
        } else {
            comparator = Comparator.comparing(Movie::getYear, Comparator.nullsLast(Integer::compareTo));
        }

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        movies = movies.stream()
                .sorted(comparator)
                .toList();

        return movies;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Integer>> sync() {
        int updated = omdbService.syncFromOmdb();
        return ResponseEntity.ok(Map.of("updated", updated));
    }
}
