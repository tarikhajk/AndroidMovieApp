package com.outlook.movieappv2.Model;

import com.google.gson.annotations.SerializedName;

public class GenreResponseResults {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    private GenreResponseResults() {
        // empty constructor
    }

    public GenreResponseResults(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}