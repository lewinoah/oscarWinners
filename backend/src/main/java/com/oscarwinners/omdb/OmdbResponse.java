package com.oscarwinners.omdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbResponse {

    @JsonProperty("Response")
    private String response;

    @JsonProperty("Error")
    private String error;

    @JsonProperty("imdbID")
    private String imdbId;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Poster")
    private String poster;

    @JsonProperty("Actors")
    private String actors;

    @JsonProperty("Director")
    private String director;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Awards")
    private String awards;

    @JsonProperty("Rated")
    private String rated;

    @JsonProperty("Runtime")
    private String runtime;

    @JsonProperty("Released")
    private String released;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Metascore")
    private String metascore;

    @JsonProperty("Ratings")
    private List<Rating> ratings;

    public boolean isSuccess() {
        return "True".equalsIgnoreCase(response);
    }

    public String getImdbId() { return imdbId; }
    public String getTitle() { return title; }
    public String getYear() { return year; }
    public String getPlot() { return plot; }
    public String getPoster() { return poster; }
    public String getActors() { return actors; }
    public String getDirector() { return director; }
    public String getGenre() { return genre; }
    public String getAwards() { return awards; }
    public String getRated() { return rated; }
    public String getRuntime() { return runtime; }
    public String getReleased() { return released; }
    public String getLanguage() { return language; }
    public String getCountry() { return country; }
    public String getMetascore() { return metascore; }
    public List<Rating> getRatings() { return ratings; }
    public String getError() { return error; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rating {
        @JsonProperty("Source")
        private String source;
        @JsonProperty("Value")
        private String value;
        public String getSource() { return source; }
        public String getValue() { return value; }
    }
}
