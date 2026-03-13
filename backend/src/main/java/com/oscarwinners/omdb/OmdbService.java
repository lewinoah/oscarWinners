package com.oscarwinners.omdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscarwinners.entity.Movie;
import com.oscarwinners.repository.MovieRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class OmdbService {

    private static final String OMDB_BASE = "https://www.omdbapi.com/";
    private static final Logger log = LoggerFactory.getLogger(OmdbService.class);
    private static final int RETRY_DELAY_MS = 1100;

    private final WebClient webClient;
    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;

    private final String apiKey;

    // #region agent log
    private static void agentDebugLog(String hypothesisId, String message, String data) {
        long ts = System.currentTimeMillis();
        String json = "{\"sessionId\":\"1cc470\",\"runId\":\"pre-fix\",\"hypothesisId\":\""
                + hypothesisId
                + "\",\"location\":\"OmdbService.java:30\",\"message\":\""
                + message.replace("\"", "'")
                + "\",\"data\":\""
                + (data == null ? "" : data.replace("\"", "'"))
                + "\",\"timestamp\":"
                + ts
                + "}";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("debug-1cc470.log", true))) {
            bw.write(json);
            bw.newLine();
        } catch (IOException ignored) {
        }
    }
    // #endregion

    public OmdbService(
            @Value("${omdb.api.key:}") String apiKey,
            MovieRepository movieRepository,
            ObjectMapper objectMapper) {
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
        String resolved = apiKey;
        if (resolved != null) {
            resolved = resolved.trim();
        }
        if (resolved == null || resolved.isBlank()) {
            Dotenv dotenv = Dotenv.configure()
                    .directory(".")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();
            String fromEnvFile = dotenv.get("OMDB_API_KEY");
            if (fromEnvFile != null && !fromEnvFile.isBlank()) {
                resolved = fromEnvFile.trim();
            }
        }
        this.apiKey = (resolved != null && !resolved.isBlank()) ? resolved : "demo";
        boolean usingDemo = "demo".equals(this.apiKey);
        int length = this.apiKey != null ? this.apiKey.length() : 0;
        agentDebugLog(
                "H_OMDB_ENV",
                "apiKeyResolved",
                "usingDemo=" + usingDemo + ",length=" + length
        );
        this.webClient = WebClient.builder().baseUrl(OMDB_BASE).build();
    }

    public Optional<OmdbResponse> fetchByTitleAndYear(String title, Integer year) {
        String uri = "?apikey=" + encode(this.apiKey) + "&t=" + encode(title) + "&y=" + (year != null ? year : "");
        System.out.println("GET fetchByTitleAndYear uri: " + uri);
        return get(uri);
    }

    public Optional<OmdbResponse> fetchByImdbId(String imdbId) {
        if (imdbId == null || imdbId.isBlank()) return Optional.empty();
        String uri = "?apikey=" + encode(this.apiKey) + "&i=" + encode(imdbId);
        System.out.println("GET uri: " + uri);
        return get(uri);
    }

    private Optional<OmdbResponse> get(String uri) {
        try {
            String json = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if (json == null) return Optional.empty();
            OmdbResponse r = objectMapper.readValue(json, OmdbResponse.class);
            return r.isSuccess() ? Optional.of(r) : Optional.empty();
        } catch (WebClientResponseException.TooManyRequests e) {
            log.warn("OMDb rate limit; retrying after {} ms", RETRY_DELAY_MS);
            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return get(uri);
        } catch (WebClientResponseException e) {
            log.warn("OMDb request failed: {}", e.getStatusCode());
            return Optional.empty();
        } catch (JsonProcessingException e) {
            log.warn("OMDb response parse error", e);
            return Optional.empty();
        }
    }

    private static String encode(String s) {
        if (s == null) return "";
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

    public int syncFromOmdb() {
        List<Movie> toSync = movieRepository.findByOmdbSyncedFalseOrOmdbSyncedIsNull();
        int updated = 0;
        for (Movie m : toSync) {
            Optional<OmdbResponse> opt = m.getImdbId() != null
                    ? fetchByImdbId(m.getImdbId())
                    : fetchByTitleAndYear(m.getTitle(), m.getYear());
            if (opt.isEmpty()) {
                log.debug("No OMDb data for {} ({})", m.getTitle(), m.getYear());
                continue;
            }
            applyToMovie(opt.get(), m);
            m.setOmdbSynced(true);
            movieRepository.save(m);
            updated++;
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return updated;
    }

    private void applyToMovie(OmdbResponse r, Movie m) {
        m.setImdbId(r.getImdbId());
        if (r.getTitle() != null) m.setTitle(r.getTitle());
        if (r.getYear() != null && !r.getYear().isBlank()) {
            try {
                m.setYear(Integer.parseInt(r.getYear().replaceAll("[^0-9]", "").substring(0, 4)));
            } catch (Exception ignored) {}
        }
        m.setPlot(r.getPlot());
        m.setPosterUrl("N/A".equalsIgnoreCase(r.getPoster()) ? null : r.getPoster());
        m.setActors(r.getActors());
        m.setDirector(r.getDirector());
        m.setGenre(r.getGenre());
        m.setAwards(r.getAwards());
        m.setRated(r.getRated());
        m.setRuntime(r.getRuntime());
        m.setReleased(r.getReleased());
        m.setLanguage(r.getLanguage());
        m.setCountry(r.getCountry());
        m.setMetascore(r.getMetascore());
        if (r.getRatings() != null && !r.getRatings().isEmpty()) {
            try {
                m.setRatingsJson(objectMapper.writeValueAsString(r.getRatings()));
            } catch (JsonProcessingException ignored) {}
        }
    }
}
