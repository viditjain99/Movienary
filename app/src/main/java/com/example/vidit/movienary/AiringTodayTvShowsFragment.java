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

import com.airbnb.lottie.LottieAnimationView;
import com.hereshem.lib.recycler.MyRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AiringTodayTvShowsFragment extends Fragment
{
    MyRecyclerView airingTodayTvRecyclerView;
    LottieAnimationView tvLoading;
    ArrayList<Tv> airingTodayTvShowsList=new ArrayList<>();
    //FloatingActionButton searchButton;
    TvAdapter adapter;
    AiringTodayTvShowsFragmentCallBack listener;
    int page=1;
    int totalPages;
    public AiringTodayTvShowsFragment() {

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof AiringTodayTvShowsFragmentCallBack)
        {
            listener=(AiringTodayTvShowsFragmentCallBack) context;
        }
        page=1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_airing_today_tvshows,container,false);
        airingTodayTvRecyclerView=output.findViewById(R.id.airingTodayTvRecyclerView);
        tvLoading=output.findViewById(R.id.tvLoading);
        //searchButton=output.findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//            }
//        });
        airingTodayTvRecyclerView.setVisibility(View.GONE);
        //searchButton.setVisibility(View.GONE);
        tvLoading.setVisibility(View.VISIBLE);

        adapter=new TvAdapter(getContext(), airingTodayTvShowsList, new TvClickListener() {
            @Override
            public void onTvClick(View view, int position)
            {
                Tv tvShow=airingTodayTvShowsList.get(position);
                if(listener!=null)
                {
                    listener.onTvShowSelected(tvShow);
                }
            }
        });
        airingTodayTvRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        airingTodayTvRecyclerView.setLayoutManager(layoutManager);
        fetchPopularTvShows(page);
        return output;
    }

    public void fetchPopularTvShows(int page)
    {
        tvLoading.setVisibility(View.VISIBLE);
        airingTodayTvRecyclerView.setVisibility(View.GONE);
        Call<TvResponse> call=ApiClient.getTvService().getPopularTvShows(page);
        page++;
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                TvResponse tvResponse=response.body();
                ArrayList<Tv> tvShowsList=tvResponse.results;
                totalPages=tvResponse.totalPages;
                airingTodayTvShowsList.clear();
                airingTodayTvShowsList.addAll(tvShowsList);
                tvLoading.setVisibility(View.GONE);
                airingTodayTvRecyclerView.setVisibility(View.VISIBLE);
                //searchButton.setVisibility(View.VISIBLE);
                airingTodayTvRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {

            }
        });
    }
    public interface AiringTodayTvShowsFragmentCallBack
    {
        void onTvShowSelected(Tv tvShow);
    }
}
