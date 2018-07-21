package com.example.vidit.movienary;


import android.content.Context;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hereshem.lib.recycler.MyRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingMoviesFragment extends Fragment {

    MyRecyclerView upcomingRecyclerView;
    ArrayList<Movie> upcomingMoviesList=new ArrayList<>();
    FloatingActionButton searchButton;
    LottieAnimationView loading;
    MovieAdapter adapter;
    UpcomingMoviesFragmentCallBack listener;
    int page=1;
    public UpcomingMoviesFragment() {

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof UpcomingMoviesFragmentCallBack)
        {
            listener=(UpcomingMoviesFragmentCallBack) context;
        }
        page=1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_upcoming_movies,container,false);
        upcomingRecyclerView=output.findViewById(R.id.upcomingRecyclerView);
        loading=output.findViewById(R.id.loading);
        searchButton=output.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
            }
        });
        upcomingRecyclerView.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        adapter=new MovieAdapter(getContext(), upcomingMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position)
            {
                Movie movie=upcomingMoviesList.get(position);
                if(listener!=null)
                {
                    listener.onMovieSelected(movie);
                }
            }
        });
        upcomingRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        upcomingRecyclerView.setLayoutManager(layoutManager);
        fetchTopRatedMovies(page);
        return output;
    }

    public void fetchTopRatedMovies(int page)
    {
        loading.setVisibility(View.VISIBLE);
        upcomingRecyclerView.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getUpcomingMovies(page);
        page++;
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                upcomingMoviesList.clear();
                upcomingMoviesList.addAll(moviesList);
                loading.setVisibility(View.GONE);
                upcomingRecyclerView.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                upcomingRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
    public interface UpcomingMoviesFragmentCallBack
    {
        void onMovieSelected(Movie movie);
    }
}
