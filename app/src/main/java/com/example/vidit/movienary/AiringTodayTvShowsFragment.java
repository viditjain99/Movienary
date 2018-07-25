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

public class AiringTodayTvShowsFragment extends Fragment
{
    MyRecyclerView airingTodayTvRecyclerView;
    LottieAnimationView tvLoading;
    ArrayList<Tv> airingTodayTvShowsList=new ArrayList<>();
    //FloatingActionButton searchButton;
    TvAdapter adapter;
    AiringTodayTvShowsFragmentCallBack listener;
    int currentPage=1;
    int totalPages;
    Button nextButton,prevButton;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_airing_today_tvshows,container,false);
        airingTodayTvRecyclerView=output.findViewById(R.id.airingTodayTvRecyclerView);
        tvLoading=output.findViewById(R.id.tvLoading);
        nextButton=output.findViewById(R.id.nextButton);
        prevButton=output.findViewById(R.id.prevButton);
        airingTodayTvShowsList.clear();
        //searchButton=output.findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//            }
//        });
        //searchButton.setVisibility(View.GONE);

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
        fetchAiringTodayTvShows(currentPage);
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
                    fetchAiringTodayTvShows(currentPage);
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
                    fetchAiringTodayTvShows(currentPage);
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

    public void fetchAiringTodayTvShows(int page)
    {
        airingTodayTvRecyclerView.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        tvLoading.setVisibility(View.VISIBLE);
        Call<TvResponse> call=ApiClient.getTvService().getAiringTodayTvShows(page);
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
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
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
