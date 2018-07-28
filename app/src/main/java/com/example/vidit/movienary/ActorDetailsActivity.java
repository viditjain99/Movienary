package com.example.vidit.movienary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hereshem.lib.recycler.MyRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorDetailsActivity extends AppCompatActivity
{
    Intent intent;
    TextView actorNameTextView,ageTextView,birthPlaceTextView,biography,movieCreditsTextView,tvShowCredits;
    ExpandableTextView biographyTextView;
    ImageView profileImageView;
    android.support.v7.widget.Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String profilePath;
    MyRecyclerView movieCreditsRecyclerView,tvCreditsRecyclerView;
    ArrayList<Movie> movieCredits=new ArrayList<>();
    ArrayList<Tv> tvCredits=new ArrayList<>();
    MovieAdapter adapter;
    TvAdapter tvAdapter;
    LottieAnimationView loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_details);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        actorNameTextView=findViewById(R.id.actorNameTextView);
        biographyTextView=findViewById(R.id.biographyTextView);
        ageTextView=findViewById(R.id.ageTextView);
        birthPlaceTextView=findViewById(R.id.birthPlaceTextView);
        profileImageView=findViewById(R.id.profileImageView);
        movieCreditsRecyclerView=findViewById(R.id.movieCreditsRecyclerView);
        tvCreditsRecyclerView=findViewById(R.id.tvCreditsRecyclerView);
        movieCreditsTextView=findViewById(R.id.movieCredits);
        tvShowCredits=findViewById(R.id.tvCredits);
        loading=findViewById(R.id.loading);
        biography=findViewById(R.id.biography);

        toolbar=findViewById(R.id.toolbar);
        collapsingToolbarLayout=findViewById(R.id.collapsingToolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter=new MovieAdapter(ActorDetailsActivity.this, movieCredits, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                Movie movie = movieCredits.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("MovieName", movie.movieName);
                bundle.putString("PosterPath", movie.posterPath);
                bundle.putString("Rating", movie.rating);
                bundle.putString("Id", movie.id);
                bundle.putString("BackdropPath", movie.backdropPath);
                bundle.putString("Overview", movie.overview);
                bundle.putString("ReleaseDate", movie.releaseDate);
                Intent intent = new Intent(ActorDetailsActivity.this, DetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tvAdapter=new TvAdapter(ActorDetailsActivity.this, tvCredits, new TvClickListener() {
            @Override
            public void onTvClick(View view, int position) {
                Tv tvShow=tvCredits.get(position);
                Bundle bundle=new Bundle();
                bundle.putString("TvShowName",tvShow.tvShowName);
                bundle.putString("PosterPath",tvShow.posterPath);
                bundle.putString("Rating",tvShow.rating);
                bundle.putString("Id",tvShow.id);
                bundle.putString("BackdropPath",tvShow.backdropPath);
                bundle.putString("Overview",tvShow.overview);
                bundle.putString("ReleaseDate",tvShow.firstAirDate);
                Intent intent=new Intent(ActorDetailsActivity.this,TvShowDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(ActorDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        movieCreditsRecyclerView.setLayoutManager(layoutManager);
        movieCreditsRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager1=new LinearLayoutManager(ActorDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        tvCreditsRecyclerView.setLayoutManager(layoutManager1);
        tvCreditsRecyclerView.setAdapter(tvAdapter);
        actorNameTextView.setVisibility(View.GONE);
        biographyTextView.setVisibility(View.GONE);
        ageTextView.setVisibility(View.GONE);
        birthPlaceTextView.setVisibility(View.GONE);
        biography.setVisibility(View.GONE);
        movieCreditsTextView.setVisibility(View.GONE);
        tvShowCredits.setVisibility(View.GONE);
        profileImageView.setVisibility(View.GONE);
        movieCreditsRecyclerView.setVisibility(View.GONE);
        tvCreditsRecyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        intent=getIntent();
        String id=intent.getStringExtra("Id");
        Call<Actor> call=ApiClient.getActorsService().getDetails(id);
        call.enqueue(new Callback<Actor>() {
            @Override
            public void onResponse(Call<Actor> call, Response<Actor> response) {
                Actor actor=response.body();
                actorNameTextView.setText(actor.name);
                biographyTextView.setText(actor.biography);
                Picasso.get().load("http://image.tmdb.org/t/p/w500"+actor.profilePath).into(profileImageView);
                birthPlaceTextView.setText("Birth Place: "+actor.placeOfBirth);
                if(actor.birthday==null)
                {
                    ageTextView.setText("Age: N/A");
                }
                else
                {
                    int year=Integer.parseInt(actor.birthday.substring(0,4));
                    int currYear= Calendar.getInstance().get(Calendar.YEAR);
                    int age=currYear-year;
                    ageTextView.setText("Age: "+age);
                }
                profilePath=actor.profilePath;
                collapsingToolbarLayout.setTitle(actor.name);
                collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
            }

            @Override
            public void onFailure(Call<Actor> call, Throwable t) {

            }
        });

        Call<MovieCreditsResponse> call1=ApiClient.getMoviesService().getMovieCredits(id);
        call1.enqueue(new Callback<MovieCreditsResponse>() {
            @Override
            public void onResponse(Call<MovieCreditsResponse> call, Response<MovieCreditsResponse> response) {
                MovieCreditsResponse movieCreditsResponse=response.body();
                ArrayList<Movie> moviesList=movieCreditsResponse.cast;
                movieCredits.addAll(moviesList);
                movieCreditsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieCreditsResponse> call, Throwable t) {

            }
        });
        Call<TvCreditsResponse> call2=ApiClient.getTvService().getTvCredits(id);
        call2.enqueue(new Callback<TvCreditsResponse>() {
            @Override
            public void onResponse(Call<TvCreditsResponse> call, Response<TvCreditsResponse> response) {
                TvCreditsResponse tvCreditsResponse=response.body();
                ArrayList<Tv> tvShowsList=tvCreditsResponse.cast;
                tvCredits.addAll(tvShowsList);
                actorNameTextView.setVisibility(View.VISIBLE);
                biographyTextView.setVisibility(View.VISIBLE);
                ageTextView.setVisibility(View.VISIBLE);
                birthPlaceTextView.setVisibility(View.VISIBLE);
                biography.setVisibility(View.VISIBLE);
                movieCreditsTextView.setVisibility(View.VISIBLE);
                tvShowCredits.setVisibility(View.VISIBLE);
                profileImageView.setVisibility(View.VISIBLE);
                tvCreditsRecyclerView.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TvCreditsResponse> call, Throwable t) {

            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=view.getId();
                if(id==R.id.profileImageView)
                {
                    Intent intent=new Intent(ActorDetailsActivity.this,ImageActivity.class);
                    intent.putExtra("BackdropPath",profilePath);
                    startActivity(intent);
                }
            }
        });
    }

}
