package com.hackathon.clipfinder.models;

import com.google.gson.annotations.SerializedName;

public class ClipSearchRequest {

    @SerializedName("movieName")
    private String movieName;

    @SerializedName("category")
    private String category;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
