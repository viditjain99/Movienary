package com.example.vidit.movienary;

import com.google.gson.annotations.SerializedName;

public class Showtime
{
    @SerializedName("cinema_id")
    String cinemaId;
    @SerializedName("movie_id")
    String movieId;
    @SerializedName("start_at")
    String startAt;
}
