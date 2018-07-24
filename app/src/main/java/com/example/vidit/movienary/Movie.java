package com.example.vidit.movienary;

import com.google.gson.annotations.SerializedName;

public class Movie
{
    @SerializedName("title")
    String movieName;
    @SerializedName("poster_path")
    String posterPath;
    @SerializedName("vote_average")
    String rating;
    String id;
    @SerializedName("backdrop_path")
    String backdropPath;
    String overview;
    @SerializedName("release_date")
    String releaseDate;
    @SerializedName("media_type")
    String mediaType;
    @SerializedName("name")
    String tvShowName;
    @SerializedName("first_air_date")
    String firstAirDate;
    boolean favourite=false;
}
