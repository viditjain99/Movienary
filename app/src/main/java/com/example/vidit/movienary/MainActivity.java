package com.example.vidit.movienary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView popularRecyclerView,topRatedRecyclerView,nowShowingRecyclerView,upcomingRecyclerView;
    MovieAdapter adapter1,adapter2,adapter3,adapter4;
    ProgressBar progressBar;
    TextView popularMovies,topRatedMovies,nowShowingMovies,upcomingMovies;
    ArrayList<Movie> popularMoviesList=new ArrayList<>();
    ArrayList<Movie> topRatedMoviesList=new ArrayList<>();
    ArrayList<Movie> nowShowingMoviesList=new ArrayList<>();
    ArrayList<Movie> upcomingMoviesList=new ArrayList<>();
    public static final String DETAILS_KEY="details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Movies");
        popularRecyclerView=findViewById(R.id.popularRecyclerView);
        topRatedRecyclerView=findViewById(R.id.topRatedRecyclerView);
        nowShowingRecyclerView=findViewById(R.id.nowShowingRecyclerView);
        upcomingRecyclerView=findViewById(R.id.upcomingRecyclerView);

        popularMovies=findViewById(R.id.popularMovies);
        topRatedMovies=findViewById(R.id.topRatedMovies);
        nowShowingMovies=findViewById(R.id.nowShowingMovies);
        upcomingMovies=findViewById(R.id.upcomingMovies);

        progressBar=findViewById(R.id.progressBar);

        adapter1=new MovieAdapter(this, popularMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position)
            {
                Movie movie=popularMoviesList.get(position);
                Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
                ArrayList<String> details=new ArrayList<>();
                details.add(movie.movieName);
                details.add(movie.posterPath);
                details.add(movie.rating);
                details.add(movie.id);
                details.add(movie.backdropPath);
                details.add(movie.overview);
                details.add(movie.releaseDate);
                intent.putExtra(DETAILS_KEY,details);
                startActivity(intent);
            }
        });
        popularRecyclerView.setAdapter(adapter1);
        adapter2=new MovieAdapter(this, topRatedMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position)
            {
                Movie movie=topRatedMoviesList.get(position);
                Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
                ArrayList<String> details=new ArrayList<>();
                details.add(movie.movieName);
                details.add(movie.posterPath);
                details.add(movie.rating);
                details.add(movie.id);
                details.add(movie.backdropPath);
                details.add(movie.overview);
                details.add(movie.releaseDate);
                intent.putExtra(DETAILS_KEY,details);
                startActivity(intent);
            }
        });
        topRatedRecyclerView.setAdapter(adapter2);
        adapter3=new MovieAdapter(this, nowShowingMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position)
            {
                Movie movie=nowShowingMoviesList.get(position);
                Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
                ArrayList<String> details=new ArrayList<>();
                details.add(movie.movieName);
                details.add(movie.posterPath);
                details.add(movie.rating);
                details.add(movie.id);
                details.add(movie.backdropPath);
                details.add(movie.overview);
                details.add(movie.releaseDate);
                intent.putExtra(DETAILS_KEY,details);
                startActivity(intent);
            }
        });
        nowShowingRecyclerView.setAdapter(adapter3);
        adapter4=new MovieAdapter(this, upcomingMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position)
            {
                Movie movie=upcomingMoviesList.get(position);
                Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
                ArrayList<String> details=new ArrayList<>();
                details.add(movie.movieName);
                details.add(movie.posterPath);
                details.add(movie.rating);
                details.add(movie.id);
                details.add(movie.backdropPath);
                details.add(movie.overview);
                details.add(movie.releaseDate);
                intent.putExtra(DETAILS_KEY,details);
                startActivity(intent);
            }
        });
        upcomingRecyclerView.setAdapter(adapter4);

        LinearLayoutManager layoutManager1=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager2=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager3=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager4=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        popularRecyclerView.setLayoutManager(layoutManager1);
        topRatedRecyclerView.setLayoutManager(layoutManager2);
        nowShowingRecyclerView.setLayoutManager(layoutManager3);
        upcomingRecyclerView.setLayoutManager(layoutManager4);

        fetchPopularMovies();
        fetchTopRatedMovies();
        fetchNowShowingMovies();
        fetchUpcomingMovies();
    }

    public void fetchPopularMovies()
    {
        progressBar.setVisibility(View.VISIBLE);
        popularRecyclerView.setVisibility(View.GONE);
        popularMovies.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getPopularMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                popularMoviesList.clear();
                popularMoviesList.addAll(moviesList);
                progressBar.setVisibility(View.GONE);
                popularRecyclerView.setVisibility(View.VISIBLE);
                popularMovies.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    public void fetchTopRatedMovies()
    {
        progressBar.setVisibility(View.VISIBLE);
        topRatedRecyclerView.setVisibility(View.GONE);
        topRatedMovies.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getTopRatedMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                topRatedMoviesList.clear();
                topRatedMoviesList.addAll(moviesList);
                progressBar.setVisibility(View.GONE);
                topRatedRecyclerView.setVisibility(View.VISIBLE);
                topRatedMovies.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    public void fetchNowShowingMovies()
    {
        progressBar.setVisibility(View.VISIBLE);
        nowShowingRecyclerView.setVisibility(View.GONE);
        nowShowingMovies.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getNowShowingMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                nowShowingMoviesList.clear();
                nowShowingMoviesList.addAll(moviesList);
                progressBar.setVisibility(View.GONE);
                nowShowingRecyclerView.setVisibility(View.VISIBLE);
                nowShowingMovies.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    public void fetchUpcomingMovies()
    {
        progressBar.setVisibility(View.VISIBLE);
        upcomingRecyclerView.setVisibility(View.GONE);
        upcomingMovies.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getUpcomingMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                upcomingMoviesList.clear();
                upcomingMoviesList.addAll(moviesList);
                progressBar.setVisibility(View.GONE);
                upcomingRecyclerView.setVisibility(View.VISIBLE);
                upcomingMovies.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
}
