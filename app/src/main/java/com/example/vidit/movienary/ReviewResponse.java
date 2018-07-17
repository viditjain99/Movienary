package com.example.vidit.movienary;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewResponse
{
    ArrayList<Review> results;
    @SerializedName("total_pages")
    int totalPages;
}
