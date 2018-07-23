package com.example.vidit.movienary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ActorsService
{
    @GET("3/person/{id}?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<Actor> getDetails(@Path("id") String id);
}
