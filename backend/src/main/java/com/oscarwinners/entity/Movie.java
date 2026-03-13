package com.oscarwinners.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imdb_id", unique = true)
    private String imdbId;

    @Column(nullable = false)
    private String title;

    private Integer year;

    @Column(length = 2000)
    private String plot;

    @Column(name = "poster_url", length = 500)
    private String posterUrl;

    @Column(length = 1000)
    private String actors;

    @Column(length = 500)
    private String director;

    @Column(length = 500)
    private String genre;

    @Column(length = 500)
    private String awards;

    @Column(length = 50)
    private String rated;

    @Column(length = 50)
    private String runtime;

    @Column(length = 100)
    private String released;

    @Column(length = 200)
    private String language;

    @Column(length = 200)
    private String country;

    @Column(length = 20)
    private String metascore;

    @Column(name = "ratings_json", length = 2000)
    private String ratingsJson;

    /** Whether OMDb data has been fetched and cached. */
    @Column(name = "omdb_synced")
    private Boolean omdbSynced = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getRatingsJson() {
        return ratingsJson;
    }

    public void setRatingsJson(String ratingsJson) {
        this.ratingsJson = ratingsJson;
    }

    public Boolean getOmdbSynced() {
        return omdbSynced;
    }

    public void setOmdbSynced(Boolean omdbSynced) {
        this.omdbSynced = omdbSynced;
    }
}
