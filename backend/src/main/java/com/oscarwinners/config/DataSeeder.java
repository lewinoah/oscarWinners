package com.oscarwinners.config;

import com.oscarwinners.entity.Movie;
import com.oscarwinners.repository.MovieRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class DataSeeder implements ApplicationRunner {

    private static final List<SeedEntry> SEED_MOVIES = List.of(
            new SeedEntry("Wings", 1929),
            new SeedEntry("All Quiet on the Western Front", 1930),
            new SeedEntry("Grand Hotel", 1932),
            new SeedEntry("It Happened One Night", 1934),
            new SeedEntry("Gone with the Wind", 1939),
            new SeedEntry("Casablanca", 1943),
            new SeedEntry("The Best Years of Our Lives", 1946),
            new SeedEntry("All About Eve", 1950),
            new SeedEntry("On the Waterfront", 1954),
            new SeedEntry("The Bridge on the River Kwai", 1957),
            new SeedEntry("Lawrence of Arabia", 1962),
            new SeedEntry("The Godfather", 1972),
            new SeedEntry("The Godfather Part II", 1974),
            new SeedEntry("One Flew Over the Cuckoo's Nest", 1975),
            new SeedEntry("Rocky", 1976),
            new SeedEntry("Annie Hall", 1977),
            new SeedEntry("The Deer Hunter", 1978),
            new SeedEntry("Kramer vs. Kramer", 1979),
            new SeedEntry("Ordinary People", 1980),
            new SeedEntry("Chariots of Fire", 1981),
            new SeedEntry("Parasite", 2019),
            new SeedEntry("CODA", 2021)
    );

    private final MovieRepository movieRepository;

    public DataSeeder(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // #region agent log
    private static void agentDebugLog(String hypothesisId, String message, String data) {
        long ts = System.currentTimeMillis();
        String json = "{\"sessionId\":\"1cc470\",\"runId\":\"pre-fix\",\"hypothesisId\":\""
                + hypothesisId
                + "\",\"location\":\"DataSeeder.java:46\",\"message\":\""
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

    @Override
    public void run(ApplicationArguments args) {
        if (movieRepository.count() > 0) {
            return;
        }
        agentDebugLog("H1", "starting-seed", "size=" + SEED_MOVIES.size());
        for (SeedEntry e : SEED_MOVIES) {
            Movie m = new Movie();
            m.setTitle(e.title());
            m.setYear(e.year());
            m.setOmdbSynced(false);
            agentDebugLog("H1", "before-save", e.title() + " (" + e.year() + ")");
            movieRepository.save(m);
        }
    }

    private record SeedEntry(String title, int year) {}
}
