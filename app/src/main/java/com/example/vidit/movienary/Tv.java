package com.example.vidit.movienary;

import com.google.gson.annotations.SerializedName;

public class Tv
{
    @SerializedName("name")
    String tvShowName;
    @SerializedName("first_air_date")
    String firstAirDate;
    @SerializedName("backdrop_path")
    String backdropPath;
    @SerializedName("poster_path")
    String posterPath;
    String overview;
    String id;
    @SerializedName("vote_average")
    String rating;
    boolean favourite=false;
}
