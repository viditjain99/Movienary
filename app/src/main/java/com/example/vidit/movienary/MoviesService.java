package com.example.vidit.movienary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesService
{
    @GET("3/movie/popular?api_key=c8e098971d85941867217bb907834115&language=en-US&page=1")
    Call<MovieResponse> getPopularMovies();

    @GET("3/movie/top_rated?api_key=c8e098971d85941867217bb907834115&language=en-US&page=1")
    Call<MovieResponse> getTopRatedMovies();

    @GET("3/movie/now_playing?api_key=c8e098971d85941867217bb907834115&language=en-US&page=1")
    Call<MovieResponse> getNowShowingMovies();

    @GET("3/movie/upcoming?api_key=c8e098971d85941867217bb907834115&language=en-US&page=1")
    Call<MovieResponse> getUpcomingMovies();

    @GET("3/movie/{id}?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<SingleMovie> getDetails(@Path("id") String id);

    @GET("3/movie/{id}/credits?api_key=c8e098971d85941867217bb907834115")
    Call<CastResponse> getCast(@Path("id") String id);
}
