package com.example.vidit.movienary;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hereshem.lib.recycler.MyRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment
{
    MyRecyclerView popularRecyclerView,topRatedRecyclerView,nowShowingRecyclerView,upcomingRecyclerView;
    MovieAdapter adapter1,adapter2,adapter3,adapter4;
    ProgressBar progressBar;
    TextView popularMovies,topRatedMovies,nowShowingMovies,upcomingMovies;
    ArrayList<Movie> popularMoviesList=new ArrayList<>();
    ArrayList<Movie> topRatedMoviesList=new ArrayList<>();
    ArrayList<Movie> nowShowingMoviesList=new ArrayList<>();
    ArrayList<Movie> upcomingMoviesList=new ArrayList<>();
    FloatingActionButton searchButton;
    int page1=1,page2=1,page3=1,page4=1;
    public static final String DETAILS_KEY="details";
    MainFragmentCallBack listener;
    public MainFragment()
    {
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof MainFragmentCallBack)
        {
            listener=(MainFragmentCallBack) context;
        }
        page1=1;
        page2=1;
        page3=1;
        page4=1;
    }
    public void fetchPopularMovies(int page)
    {
        progressBar.setVisibility(View.VISIBLE);
        popularRecyclerView.setVisibility(View.GONE);
        popularMovies.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getPopularMovies(page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                popularMoviesList.addAll(moviesList);
                progressBar.setVisibility(View.GONE);
                popularRecyclerView.setVisibility(View.VISIBLE);
                popularMovies.setVisibility(View.VISIBLE);
                page1++;
                popularRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    public void fetchTopRatedMovies(int page)
    {
        progressBar.setVisibility(View.VISIBLE);
        topRatedRecyclerView.setVisibility(View.GONE);
        topRatedMovies.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getTopRatedMovies(page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                topRatedMoviesList.addAll(moviesList);
                progressBar.setVisibility(View.GONE);
                topRatedRecyclerView.setVisibility(View.VISIBLE);
                topRatedMovies.setVisibility(View.VISIBLE);
                page2++;
                topRatedRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    public void fetchNowShowingMovies(int page)
    {
        progressBar.setVisibility(View.VISIBLE);
        nowShowingRecyclerView.setVisibility(View.GONE);
        nowShowingMovies.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getNowShowingMovies(page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                nowShowingMoviesList.addAll(moviesList);
                progressBar.setVisibility(View.GONE);
                nowShowingRecyclerView.setVisibility(View.VISIBLE);
                nowShowingMovies.setVisibility(View.VISIBLE);
                page3++;
                nowShowingRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    public void fetchUpcomingMovies(int page)
    {
        progressBar.setVisibility(View.VISIBLE);
        upcomingRecyclerView.setVisibility(View.GONE);
        upcomingMovies.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getUpcomingMovies(page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> moviesList=movieResponse.results;
                upcomingMoviesList.addAll(moviesList);
                progressBar.setVisibility(View.GONE);
                upcomingRecyclerView.setVisibility(View.VISIBLE);
                upcomingMovies.setVisibility(View.VISIBLE);
                page4++;
                upcomingRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View output=inflater.inflate(R.layout.fragment_main,container,false);
        popularRecyclerView=output.findViewById(R.id.popularRecyclerView);
        topRatedRecyclerView=output.findViewById(R.id.topRatedRecyclerView);
        nowShowingRecyclerView=output.findViewById(R.id.nowShowingRecyclerView);
        upcomingRecyclerView=output.findViewById(R.id.upcomingRecyclerView);

        popularMovies=output.findViewById(R.id.popularMovies);
        topRatedMovies=output.findViewById(R.id.topRatedMovies);
        nowShowingMovies=output.findViewById(R.id.nowShowingMovies);
        upcomingMovies=output.findViewById(R.id.upcomingMovies);

        progressBar=output.findViewById(R.id.progressBar);

        searchButton=output.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int id=view.getId();
                if(id==R.id.searchButton)
                {
                    Intent intent=new Intent(getContext(),SearchActivity.class);
                    startActivity(intent);
                }
            }
        });

        adapter1=new MovieAdapter(getContext(), popularMoviesList, new MovieClickListener() {
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
        popularRecyclerView.setAdapter(adapter1);
        popularRecyclerView.setOnLoadMoreListener(new MyRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore()
            {
                fetchPopularMovies(page1);
            }
        });
        adapter2=new MovieAdapter(getContext(), topRatedMoviesList, new MovieClickListener() {
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
        topRatedRecyclerView.setAdapter(adapter2);
        topRatedRecyclerView.setOnLoadMoreListener(new MyRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                fetchTopRatedMovies(page2);
            }
        });
        adapter3=new MovieAdapter(getContext(), nowShowingMoviesList, new MovieClickListener() {
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
        nowShowingRecyclerView.setAdapter(adapter3);
        nowShowingRecyclerView.setOnLoadMoreListener(new MyRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                fetchNowShowingMovies(page3);
            }
        });
        adapter4=new MovieAdapter(getContext(), upcomingMoviesList, new MovieClickListener() {
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
        upcomingRecyclerView.setAdapter(adapter4);
        upcomingRecyclerView.setOnLoadMoreListener(new MyRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                fetchUpcomingMovies(page4);
            }
        });

        LinearLayoutManager layoutManager1=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager2=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager3=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager4=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        popularRecyclerView.setLayoutManager(layoutManager1);
        topRatedRecyclerView.setLayoutManager(layoutManager2);
        nowShowingRecyclerView.setLayoutManager(layoutManager3);
        upcomingRecyclerView.setLayoutManager(layoutManager4);

        fetchPopularMovies(page1);
        fetchTopRatedMovies(page2);
        fetchNowShowingMovies(page3);
        fetchUpcomingMovies(page4);

        return output;
    }
    public interface MainFragmentCallBack
    {
        void onMovieSelected(Movie movie);
    }
}
