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

public class TrendingTvShowsFragment extends Fragment 
{
    MyRecyclerView trendingTvShowsRecyclerView;
    LottieAnimationView tvLoading;
    static ArrayList<Tv> trendingTvShowsList=new ArrayList<>();
    TvAdapter adapter;
    TrendingTvShowsFragment.TrendingTvShowsFragmentCallBack listener;
    int totalPages;
    int currentPage=1;
    FloatingActionButton nextButton,prevButton;
    public TrendingTvShowsFragment() {

    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof TrendingTvShowsFragment.TrendingTvShowsFragmentCallBack)
        {
            listener=(TrendingTvShowsFragment.TrendingTvShowsFragmentCallBack) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_trending_tv_shows,container,false);
        trendingTvShowsRecyclerView=output.findViewById(R.id.trendingTvShowsRecyclerView);
        trendingTvShowsList.clear();
        tvLoading=output.findViewById(R.id.loading);
        nextButton=output.findViewById(R.id.nextButton);
        prevButton=output.findViewById(R.id.prevButton);
        nextButton.setAlpha(0.75f);
        prevButton.setAlpha(0.75f);

        adapter=new TvAdapter(getContext(), trendingTvShowsList, new TvClickListener() {
            @Override
            public void onTvClick(View view, int position) {
                Tv tvShow = trendingTvShowsList.get(position);
                if (listener != null) {
                    listener.onTvShowSelected(tvShow);
                }
            }
        });
        trendingTvShowsRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2, StaggeredGridLayoutManager.VERTICAL,false);
        trendingTvShowsRecyclerView.setLayoutManager(layoutManager);
        fetchTrendingTvShows(currentPage);
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
                    fetchTrendingTvShows(currentPage);
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
                    fetchTrendingTvShows(currentPage);
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

    public void fetchTrendingTvShows(int page)
    {
        tvLoading.setVisibility(View.VISIBLE);
        trendingTvShowsRecyclerView.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        Call<TvResponse> call=ApiClient.getTvService().getTrending(page);
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                TvResponse tvResponse=response.body();
                ArrayList<Tv> tvShowsList=tvResponse.results;
                totalPages=tvResponse.totalPages;
                trendingTvShowsList.clear();
                trendingTvShowsList.addAll(tvShowsList);
                tvLoading.setVisibility(View.GONE);
                trendingTvShowsRecyclerView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
                trendingTvShowsRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {

            }
        });
    }
    public interface TrendingTvShowsFragmentCallBack
    {
        void onTvShowSelected(Tv tvShow);
    }
}

