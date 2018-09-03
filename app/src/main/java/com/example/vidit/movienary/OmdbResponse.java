package com.example.vidit.movienary;

import android.media.Rating;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OmdbResponse
{
    @SerializedName("Ratings")
    ArrayList<Ratings> ratings;
    @SerializedName("Plot")
    String plot;
    @SerializedName("Awards")
    String awards;
    @SerializedName("Director")
    String director;
    @SerializedName("Production")
    String production;
    String totalSeasons;
    @SerializedName("Writer")
    String writer;
    @SerializedName("Response")
    String response;
}
