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
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.hereshem.lib.recycler.MyRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularTvShowsFragment extends Fragment
{
    MyRecyclerView popularTvRecyclerView;
    LottieAnimationView tvLoading;
    static ArrayList<Tv> popularTvShowsList=new ArrayList<>();
    //FloatingActionButton searchButton;
    TvAdapter adapter;
    PopularTvShowsFragmentCallBack listener;
    int currentPage=1;
    int totalPages;
    Button nextButton,prevButton;
    public PopularTvShowsFragment() {

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof PopularTvShowsFragmentCallBack)
        {
            listener=(PopularTvShowsFragmentCallBack) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_popular_tvshows,container,false);
        popularTvRecyclerView=output.findViewById(R.id.popularTvRecyclerView);
        tvLoading=output.findViewById(R.id.tvLoading);
        nextButton=output.findViewById(R.id.nextButton);
        prevButton=output.findViewById(R.id.prevButton);
        adapter=new TvAdapter(getContext(), popularTvShowsList, new TvClickListener() {
            @Override
            public void onTvClick(View view, int position)
            {
                Tv tvShow=popularTvShowsList.get(position);
                if(listener!=null)
                {
                    listener.onTvShowSelected(tvShow);
                }
            }
        });
        popularTvRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        popularTvRecyclerView.setLayoutManager(layoutManager);
        fetchPopularTvShows(currentPage);
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
                    fetchPopularTvShows(currentPage);
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
                    fetchPopularTvShows(currentPage);
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

    public void fetchPopularTvShows(int page)
    {
        popularTvRecyclerView.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        tvLoading.setVisibility(View.VISIBLE);
        Call<TvResponse> call=ApiClient.getTvService().getPopularTvShows(page);
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                TvResponse tvResponse=response.body();
                ArrayList<Tv> tvShowsList=tvResponse.results;
                totalPages=tvResponse.totalPages;
                popularTvShowsList.clear();
                popularTvShowsList.addAll(tvShowsList);
                tvLoading.setVisibility(View.GONE);
                popularTvRecyclerView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
                popularTvRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {

            }
        });
    }
    public interface PopularTvShowsFragmentCallBack
    {
        void onTvShowSelected(Tv tvShow);
    }
}

