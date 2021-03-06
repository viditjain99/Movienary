package com.example.vidit.movienary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService
{
    @GET("3/movie/popular?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<MovieResponse> getPopularMovies(@Query("page") int page);

    @GET("3/movie/top_rated?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<MovieResponse> getTopRatedMovies(@Query("page") int page);

    @GET("3/movie/now_playing?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<MovieResponse> getNowShowingMovies(@Query("page") int page);

    @GET("3/movie/upcoming?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<MovieResponse> getUpcomingMovies(@Query("page") int page);

    @GET("3/movie/{id}?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<SingleMovie> getDetails(@Path("id") String id);

    @GET("3/movie/{id}/credits?api_key=c8e098971d85941867217bb907834115")
    Call<CastResponse> getCast(@Path("id") String id);

    @GET("3/movie/{id}/reviews?api_key=c8e098971d85941867217bb907834115")
    Call<ReviewResponse> getReviews(@Path("id") String id);

    @GET("3/search/multi?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<MovieResponse> getSearchResults(@Query("query") String query);

    @GET("3/movie/{id}/similar?api_key=c8e098971d85941867217bb907834115&language=en-US&page=1")
    Call<MovieResponse> getSimilarMovies(@Path("id") String id);

    @GET("3/person/{id}/movie_credits?api_key=c8e098971d85941867217bb907834115&language=en-US")
    Call<MovieCreditsResponse> getMovieCredits(@Path("id") String id);

    @GET("3/movie/{id}/external_ids?api_key=c8e098971d85941867217bb907834115")
    Call<ExternalIdResponse> getExternalIds(@Path("id") String id);

    @GET("http://www.omdbapi.com/?apikey=fa4a9295&plot=full")
    Call<OmdbResponse> getOmdbDetails(@Query("t") String name);

    @GET("3/trending/movie/day?api_key=c8e098971d85941867217bb907834115")
    Call<MovieResponse> getTrending(@Query("page") int page);
}
