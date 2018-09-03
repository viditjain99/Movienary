package com.example.vidit.movienary;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.hereshem.lib.recycler.MyRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingMoviesFragment extends Fragment
{
    MyRecyclerView trendingMoviesRecyclerView;
    LottieAnimationView loading;
    static ArrayList<Movie> trendingMoviesList=new ArrayList<>();
    MovieAdapter adapter;
    TrendingMoviesFragment.TrendingMoviesFragmentCallBack listener;
    int totalPages;
    int currentPage=1;
    FloatingActionButton nextButton,prevButton;
    public TrendingMoviesFragment() {

    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof TrendingMoviesFragment.TrendingMoviesFragmentCallBack)
        {
            listener=(TrendingMoviesFragment.TrendingMoviesFragmentCallBack) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_trending_movies,container,false);
        trendingMoviesRecyclerView=output.findViewById(R.id.trendingMoviesRecyclerView);
        trendingMoviesList.clear();
        loading=output.findViewById(R.id.loading);
        nextButton=output.findViewById(R.id.nextButton);
        prevButton=output.findViewById(R.id.prevButton);
        nextButton.setAlpha(0.75f);
        prevButton.setAlpha(0.75f);

        adapter=new MovieAdapter(getContext(), trendingMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                Movie movie = trendingMoviesList.get(position);
                if (listener != null) {
                    listener.onMovieSelected(movie);
                }
            }
        });
        trendingMoviesRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2, StaggeredGridLayoutManager.VERTICAL,false);
        trendingMoviesRecyclerView.setLayoutManager(layoutManager);
        fetchPopularMovies(currentPage);
        if(currentPage<totalPages)
        {
            nextButton.setEnabled(true);
        }
        if(currentPage==totalPages)
        {
            nextButton.setEnabled(false);
        }
        if(currentPage==1)
        {
            prevButton.setEnabled(false);
        }
        if(currentPage>1)
        {
            prevButton.setEnabled(true);
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int id=view.getId();
                if(id==R.id.nextButton)
                {
                    currentPage++;
                    fetchPopularMovies(currentPage);
                    adapter.notifyDataSetChanged();
                    if(currentPage<totalPages)
                    {
                        nextButton.setEnabled(true);
                    }
                    if(currentPage==totalPages)
                    {
                        nextButton.setEnabled(false);
                    }
                    if(currentPage==1)
                    {
                        prevButton.setEnabled(false);
                    }
                    if(currentPage>1)
                    {
                        prevButton.setEnabled(true);
                    }
                }
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=view.getId();
                if(id==R.id.prevButton)
                {
                    currentPage--;
                    fetchPopularMovies(currentPage);
                    adapter.notifyDataSetChanged();
                    if(currentPage<totalPages)
                    {
                        nextButton.setEnabled(true);
                    }
                    if(currentPage==totalPages)
                    {
                        nextButton.setEnabled(false);
                    }
                    if(currentPage==1)
                    {
                        prevButton.setEnabled(false);
                    }
                    if(currentPage>1)
                    {
                        prevButton.setEnabled(true);
                    }
                }
            }
        });
        return output;
    }

    public void fetchPopularMovies(int page)
    {
        loading.setVisibility(View.VISIBLE);
        trendingMoviesRecyclerView.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getTrending(page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                totalPages=movieResponse.totalPages;
                trendingMoviesList.clear();
                trendingMoviesList.addAll(moviesList);
                loading.setVisibility(View.GONE);
                trendingMoviesRecyclerView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
                trendingMoviesRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
    public interface TrendingMoviesFragmentCallBack
    {
        void onMovieSelected(Movie movie);
    }
}
