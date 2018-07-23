package com.example.vidit.movienary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    private static Retrofit retrofit;
    private static MoviesService service;
    private static TvService tvService;
    private static ActorsService actorsService;
    static Retrofit getInstance()
    {
        if(retrofit==null)
        {
            Retrofit.Builder builder=new Retrofit.Builder().baseUrl("https://api.themoviedb.org/").addConverterFactory(GsonConverterFactory.create());
            retrofit=builder.build();
        }
        return retrofit;
    }
    static MoviesService getMoviesService()
    {
        if(service==null)
        {
            service=getInstance().create(MoviesService.class);
        }
        return service;
    }
    static TvService getTvService()
    {
        if(tvService==null)
        {
            tvService=getInstance().create(TvService.class);
        }
        return tvService;
    }
    static ActorsService getActorsService()
    {
        if(actorsService==null)
        {
            actorsService=getInstance().create(ActorsService.class);
        }
        return actorsService;
    }
}
