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

public class OnTheAirTvShowsFragment extends Fragment
{
    MyRecyclerView onTheAirTvRecyclerView;
    LottieAnimationView tvLoading;
    ArrayList<Tv> onTheAirTvShowsList=new ArrayList<>();
    //FloatingActionButton searchButton;
    TvAdapter adapter;
    OnTheAirTvShowsFragmentCallBack listener;
    int currentPage=1;
    int totalPages;
    FloatingActionButton nextButton,prevButton;
    public OnTheAirTvShowsFragment() {

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof OnTheAirTvShowsFragmentCallBack)
        {
            listener=(OnTheAirTvShowsFragmentCallBack) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_on_the_air_tvshows,container,false);
        onTheAirTvRecyclerView=output.findViewById(R.id.onTheAirTvRecyclerView);
        nextButton=output.findViewById(R.id.nextButton);
        prevButton=output.findViewById(R.id.prevButton);
        nextButton.setAlpha(0.75f);
        prevButton.setAlpha(0.75f);
        tvLoading=output.findViewById(R.id.tvLoading);
        //searchButton=output.findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                Intent intent=new Intent(getActivity(),SearchActivity.class);
//                startActivity(intent);
//            }
//        });
        adapter=new TvAdapter(getContext(), onTheAirTvShowsList, new TvClickListener() {
            @Override
            public void onTvClick(View view, int position)
            {
                Tv tvShow=onTheAirTvShowsList.get(position);
                if(listener!=null)
                {
                    listener.onTvShowSelected(tvShow);
                }
            }
        });
        onTheAirTvRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        onTheAirTvRecyclerView.setLayoutManager(layoutManager);
        fetchOnTheAirTvShows(currentPage);
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
                    fetchOnTheAirTvShows(currentPage);
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
                    fetchOnTheAirTvShows(currentPage);
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

    public void fetchOnTheAirTvShows(int page)
    {
        tvLoading.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        onTheAirTvRecyclerView.setVisibility(View.GONE);
        Call<TvResponse> call=ApiClient.getTvService().getOnTheAirTvShows(page);
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                TvResponse tvResponse=response.body();
                ArrayList<Tv> tvShowsList=tvResponse.results;
                totalPages=tvResponse.totalPages;
                onTheAirTvShowsList.clear();
                onTheAirTvShowsList.addAll(tvShowsList);
                tvLoading.setVisibility(View.GONE);
                onTheAirTvRecyclerView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
                //searchButton.setVisibility(View.VISIBLE);
                onTheAirTvRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {

            }
        });
    }
    public interface OnTheAirTvShowsFragmentCallBack
    {
        void onTvShowSelected(Tv tvShow);
    }
}
