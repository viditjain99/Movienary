package com.example.vidit.movienary;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ActorsResponse
{
    ArrayList<Actor> results;
    @SerializedName("total_pages")
    int totalPages;
}
