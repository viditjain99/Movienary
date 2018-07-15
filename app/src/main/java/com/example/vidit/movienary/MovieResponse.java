package com.example.vidit.movienary;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieResponse
{
    int page;
    @SerializedName("total_results")
    int totalResults;
    @SerializedName("total_pages")
    int totalPages;
    ArrayList<Movie> results;
}
