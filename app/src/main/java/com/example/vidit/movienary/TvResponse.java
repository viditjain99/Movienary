package com.example.vidit.movienary;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvResponse
{
    int page;
    @SerializedName("total_pages")
    int totalPages;
    ArrayList<Tv> results;
}
