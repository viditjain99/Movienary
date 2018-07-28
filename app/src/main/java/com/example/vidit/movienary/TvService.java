package com.example.vidit.movienary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TvService
{
    @GET("3/tv/popular?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<TvResponse> getPopularTvShows(@Query("page") int page);

    @GET("3/tv/on_the_air?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<TvResponse> getOnTheAirTvShows(@Query("page") int page);

    @GET("3/tv/top_rated?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<TvResponse> getTopRatedTvShows(@Query("page") int page);

    @GET("3/tv/airing_today?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<TvResponse> getAiringTodayTvShows(@Query("page") int page);

    @GET("3/tv/{id}?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<SingleMovie> getDetails(@Path("id") String id);

    @GET("3/tv/{id}/credits?api_key=c8e098971d85941867217bb907834115")
    Call<CastResponse> getCast(@Path("id") String id);

    @GET("3/tv/{id}/reviews?api_key=c8e098971d85941867217bb907834115")
    Call<ReviewResponse> getReviews(@Path("id") String id);

    @GET("3/tv/{id}/similar?api_key=c8e098971d85941867217bb907834115&language=en-US&page=1")
    Call<TvResponse> getSimilarTvShows(@Path("id") String id);

    @GET("3/person/{id}/tv_credits?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<TvCreditsResponse> getTvCredits(@Path("id") String id);
}
