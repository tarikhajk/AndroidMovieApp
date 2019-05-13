package com.outlook.movieappv2.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenreResponse {

    @SerializedName("genres")
    private List<GenreResponseResults> genres;

    private GenreResponse() {
        // empty constructor
    }

    public GenreResponse(List<GenreResponseResults> genres) {
        this.genres = genres;
    }

    public List<GenreResponseResults> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreResponseResults> genres) {
        this.genres = genres;
    }

    public GenreResponseResults getGenreObjectFromSpinnerText(String text) {
        for (GenreResponseResults each : genres) {
            if (each.getName().equals(text)) return each;
        }
        return null;
    }
}
