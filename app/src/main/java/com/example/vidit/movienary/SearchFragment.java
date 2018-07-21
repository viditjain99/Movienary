package com.example.vidit.movienary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment
{
    RecyclerView searchResultsRecyclerView;
    SearchAdapter adapter;
    ArrayList<Movie> searchResults=new ArrayList<>();
    EditText searchEditText;
    ProgressBar progressBar;

    public SearchFragment()
    {

    }
    public void fetchData(String query)
    {
        progressBar.setVisibility(View.VISIBLE);
        searchResultsRecyclerView.setVisibility(View.GONE);
        Call<MovieResponse> call=ApiClient.getMoviesService().getSearchResults(query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> fetchResults=movieResponse.results;
                searchResults.clear();
                searchResults.addAll(fetchResults);
                progressBar.setVisibility(View.GONE);
                searchResultsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View output=inflater.inflate(R.layout.fragment_search,container,false);
        searchEditText=output.findViewById(R.id.searchEditText);
        searchResultsRecyclerView=output.findViewById(R.id.searchResultsRecyclerView);
        progressBar=output.findViewById(R.id.progressBar);
        adapter=new SearchAdapter(searchResults,getContext());
        searchResultsRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        //LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        searchResultsRecyclerView.setLayoutManager(layoutManager);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId==EditorInfo.IME_ACTION_SEARCH)
                {
                    String query=searchEditText.getText().toString();
                    fetchData(query);
                    return true;
                }
                return false;
            }
        });
        return output;
    }
}
