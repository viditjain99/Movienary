package com.example.vidit.movienary;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class PopularMoviesFragment extends Fragment{

    MyRecyclerView popularRecyclerView;
    LottieAnimationView loading;
    ArrayList<Movie> popularMoviesList=new ArrayList<>();
    //FloatingActionButton searchButton;
    MovieAdapter adapter;
    PopularMoviesFragmentCallBack listener;
    int page=1;
    int totalPages;
    EndlessRecyclerViewScrollListener scrollListener;
    public PopularMoviesFragment() {

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof PopularMoviesFragmentCallBack)
        {
            listener=(PopularMoviesFragmentCallBack) context;
        }
        page=1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_popular_movies,container,false);
        popularRecyclerView=output.findViewById(R.id.popularRecyclerView);
        popularMoviesList.clear();
        loading=output.findViewById(R.id.loading);
        //searchButton=output.findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                Intent intent=new Intent(getActivity(),SearchActivity.class);
//                startActivity(intent);
//            }
//        });
        popularRecyclerView.setVisibility(View.GONE);
        //searchButton.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        adapter=new MovieAdapter(getContext(), popularMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position)
            {
                Movie movie=popularMoviesList.get(position);
                if(listener!=null)
                {
                    listener.onMovieSelected(movie);
                }
            }
        });
        popularRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        popularRecyclerView.setLayoutManager(layoutManager);
//        scrollListener=new EndlessRecyclerViewScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                fetchPopularMovies(page);
//                adapter.notifyItemRangeInserted(0,totalItemsCount);
//            }
//        };
//        popularRecyclerView.addOnScrollListener(scrollListener);
        fetchPopularMovies(page);
        return output;
    }

    public void fetchPopularMovies(int page)
    {
        //if(page==1)
        {
            loading.setVisibility(View.VISIBLE);
            popularRecyclerView.setVisibility(View.GONE);
        }
        Call<MovieResponse> call=ApiClient.getMoviesService().getPopularMovies(page);
        //page++;
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                totalPages=movieResponse.totalPages;
                popularMoviesList.addAll(moviesList);
                loading.setVisibility(View.GONE);
                popularRecyclerView.setVisibility(View.VISIBLE);
                //searchButton.setVisibility(View.VISIBLE);
                popularRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
    public interface PopularMoviesFragmentCallBack
    {
        void onMovieSelected(Movie movie);
    }
}