package com.example.vidit.movienary;

import com.google.gson.annotations.SerializedName;

public class Actor
{
    String birthday;
    String name;
    String biography;
    @SerializedName("place_of_birth")
    String placeOfBirth;
    @SerializedName("profile_path")
    String profilePath;
}
