package com.example.vidit.movienary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VideoService
{
    @GET("3/movie/{id}/videos?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<VideoResponse> getVideos(@Path("id") String id);

    @GET("3/tv/{id}/videos?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<VideoResponse> getTvShowVideos(@Path("id") String id);
}
