package com.example.vidit.movienary;


import android.content.Context;
import android.content.Intent;
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
public class NowShowingMoviesFragment extends Fragment {


    MyRecyclerView nowShowingRecyclerView;
    ArrayList<Movie> nowShowingMoviesList=new ArrayList<>();
    FloatingActionButton searchButton;
    LottieAnimationView loading;
    MovieAdapter adapter;
    NowShowingMoviesFragmentCallBack listener;
    int page=1;
    public NowShowingMoviesFragment() {

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof NowShowingMoviesFragmentCallBack)
        {
            listener=(NowShowingMoviesFragmentCallBack) context;
        }
        page=1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_now_showing_movies,container,false);
        nowShowingRecyclerView=output.findViewById(R.id.nowShowingRecyclerView);
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

        adapter=new MovieAdapter(getContext(), nowShowingMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position)
            {
                Movie movie=nowShowingMoviesList.get(position);
                if(listener!=null)
                {
                    listener.onMovieSelected(movie);
                }
            }
        });
        nowShowingRecyclerView.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        nowShowingRecyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        nowShowingRecyclerView.setLayoutManager(layoutManager);
        fetchPopularMovies(page);
        return output;
    }

    public void fetchPopularMovies(int page)
    {
        loading.setVisibility(View.VISIBLE);
        nowShowingRecyclerView.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getNowShowingMovies(page);
        page++;
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                nowShowingMoviesList.clear();
                nowShowingMoviesList.addAll(moviesList);
                loading.setVisibility(View.GONE);
                nowShowingRecyclerView.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                nowShowingRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
    public interface NowShowingMoviesFragmentCallBack
    {
        void onMovieSelected(Movie movie);
    }
}
