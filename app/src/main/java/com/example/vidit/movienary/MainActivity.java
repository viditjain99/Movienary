package com.example.vidit.movienary;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hereshem.lib.recycler.MyRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentCallBack {

    public static final String DETAILS_KEY="details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Movies");
    }

    @Override
    public void onMovieSelected(Movie movie)
    {
        Bundle bundle=new Bundle();
        bundle.putString("MovieName",movie.movieName);
        bundle.putString("PosterPath",movie.posterPath);
        bundle.putString("Rating",movie.rating);
        bundle.putString("Id",movie.id);
        bundle.putString("BackdropPath",movie.backdropPath);
        bundle.putString("Overview",movie.overview);
        bundle.putString("ReleaseDate",movie.releaseDate);
        Intent intent=new Intent(this,DetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
