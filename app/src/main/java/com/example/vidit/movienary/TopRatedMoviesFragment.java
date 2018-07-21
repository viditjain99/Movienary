package com.example.vidit.movienary;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
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
public class TopRatedMoviesFragment extends Fragment {


    MyRecyclerView topRatedRecyclerView;
    ArrayList<Movie> topRatedMoviesList=new ArrayList<>();
    FloatingActionButton searchButton;
    LottieAnimationView loading;
    MovieAdapter adapter;
    TopRatedMoviesFragmentCallBack listener;
    int page=1;
    public TopRatedMoviesFragment() {

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof TopRatedMoviesFragmentCallBack)
        {
            listener=(TopRatedMoviesFragmentCallBack) context;
        }
        page=1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_top_rated_movies,container,false);
        topRatedRecyclerView=output.findViewById(R.id.topRatedRecyclerView);
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
        topRatedRecyclerView.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        adapter=new MovieAdapter(getContext(), topRatedMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position)
            {
                Movie movie=topRatedMoviesList.get(position);
                if(listener!=null)
                {
                    listener.onMovieSelected(movie);
                }
            }
        });
        topRatedRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        topRatedRecyclerView.setLayoutManager(layoutManager);
        fetchTopRatedMovies(page);
        return output;
    }

    public void fetchTopRatedMovies(int page)
    {
        loading.setVisibility(View.VISIBLE);
        topRatedRecyclerView.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getTopRatedMovies(page);
        page++;
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                topRatedMoviesList.clear();
                topRatedMoviesList.addAll(moviesList);
                loading.setVisibility(View.GONE);
                topRatedRecyclerView.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                topRatedRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
    public interface TopRatedMoviesFragmentCallBack
    {
        void onMovieSelected(Movie movie);
    }
}
