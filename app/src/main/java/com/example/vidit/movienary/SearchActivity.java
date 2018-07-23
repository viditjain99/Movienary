package com.example.vidit.movienary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hereshem.lib.recycler.MyRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity
{
    ArrayList<Movie> searchResults=new ArrayList<>();
    LottieAnimationView loading;
    MyRecyclerView searchResultsRecyclerView;
    SearchAdapter adapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        loading=findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        searchResultsRecyclerView=findViewById(R.id.searchResultsRecyclerView);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        //toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchResultsRecyclerView.setVisibility(View.GONE);
        adapter=new SearchAdapter(searchResults,SearchActivity.this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.VERTICAL,false);
        searchResultsRecyclerView.setAdapter(adapter);
        searchResultsRecyclerView.setLayoutManager(layoutManager);
        Intent intent=getIntent();
        String query=intent.getStringExtra("Query");
        getSupportActionBar().setTitle(query);
        Call<MovieResponse> call=ApiClient.getMoviesService().getSearchResults(query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
            {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> results=movieResponse.results;
                searchResults.addAll(results);
                searchResultsRecyclerView.setVisibility(View.VISIBLE);
                //Toast.makeText(SearchActivity.this,searchResults.get(0).posterPath,Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
}
