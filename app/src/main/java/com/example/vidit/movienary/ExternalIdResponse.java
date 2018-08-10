package com.example.vidit.movienary;

import com.google.gson.annotations.SerializedName;

public class ExternalIdResponse
{
    @SerializedName("imdb_id")
    String imdbId;
    @SerializedName("facebook_id")
    String facebookId;
    @SerializedName("twitter_id")
    String twitterId;
    @SerializedName("instagram_id")
    String instagramId;
}
