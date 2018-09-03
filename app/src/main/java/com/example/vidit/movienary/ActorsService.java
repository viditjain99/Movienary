package com.example.vidit.movienary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ActorsService
{
    @GET("3/person/{id}?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<Actor> getDetails(@Path("id") String id);

    @GET("3/person/{id}/external_ids?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<ExternalIdResponse> getExternalIds(@Path("id") String id);

    @GET("3/person/popular?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<ActorsResponse> getActors(@Query("page") int page);

    @GET("3/trending/person/day?api_key=c8e098971d85941867217bb907834115")
    Call<ActorsResponse> getTrending(@Query("page") int page);
}
