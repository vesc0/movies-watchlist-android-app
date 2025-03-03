package com.example.moviesapp;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {
    private int id;
    private String title;

    @SerializedName("poster_path") // Maps TMDB API response
    private String posterPath; // Firestore stores as "posterPath"

    @SerializedName("vote_average")
    private double voteAverage;

    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    public Movie() {
        // Empty constructor needed for Firestore deserialization
    }

    public String getReleaseYear() {
        if (releaseDate != null && !releaseDate.isEmpty()) {
            return releaseDate.split("-")[0];
        }
        return "Unknown";
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
