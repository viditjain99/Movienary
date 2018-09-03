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

public class TrendingPeopleFragment extends Fragment
{
    MyRecyclerView trendingPeopleRecyclerView;
    LottieAnimationView loading;
    static ArrayList<Actor> trendingPeopleList=new ArrayList<>();
    ActorAdapter adapter;
    TrendingPeopleFragment.TrendingPeopleFragmentCallBack listener;
    int totalPages;
    int currentPage=1;
    FloatingActionButton nextButton,prevButton;
    public TrendingPeopleFragment() {

    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof TrendingPeopleFragment.TrendingPeopleFragmentCallBack)
        {
            listener=(TrendingPeopleFragment.TrendingPeopleFragmentCallBack) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View output=inflater.inflate(R.layout.fragment_trending_people,container,false);
        trendingPeopleRecyclerView=output.findViewById(R.id.trendingPeopleRecyclerView);
        trendingPeopleList.clear();
        loading=output.findViewById(R.id.loading);
        nextButton=output.findViewById(R.id.nextButton);
        prevButton=output.findViewById(R.id.prevButton);
        nextButton.setAlpha(0.75f);
        prevButton.setAlpha(0.75f);

        adapter=new ActorAdapter(getContext(), trendingPeopleList, new ActorClickListener() {
            @Override
            public void onActorClick(View view, int position) {
                Actor actor = trendingPeopleList.get(position);
                if (listener != null) {
                    listener.onActorSelected(actor);
                }
            }
        });
        trendingPeopleRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2, StaggeredGridLayoutManager.VERTICAL,false);
        trendingPeopleRecyclerView.setLayoutManager(layoutManager);
        fetchTrendingPeople(currentPage);
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
                    fetchTrendingPeople(currentPage);
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
                    fetchTrendingPeople(currentPage);
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

    public void fetchTrendingPeople(int page)
    {
        loading.setVisibility(View.VISIBLE);
        trendingPeopleRecyclerView.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        prevButton.setVisibility(View.GONE);
        Call<ActorsResponse> call=ApiClient.getActorsService().getTrending(page);
        call.enqueue(new Callback<ActorsResponse>() {
            @Override
            public void onResponse(Call<ActorsResponse> call, Response<ActorsResponse> response) {
                ActorsResponse actorsResponse=response.body();
                ArrayList<Actor> actorsList=actorsResponse.results;
                totalPages=actorsResponse.totalPages;
                trendingPeopleList.clear();
                trendingPeopleList.addAll(actorsList);
                loading.setVisibility(View.GONE);
                trendingPeopleRecyclerView.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                prevButton.setVisibility(View.VISIBLE);
                trendingPeopleRecyclerView.loadComplete();
            }

            @Override
            public void onFailure(Call<ActorsResponse> call, Throwable t) {

            }
        });
    }
    public interface TrendingPeopleFragmentCallBack
    {
        void onActorSelected(Actor actor);
    }
}
