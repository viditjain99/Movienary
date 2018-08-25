package com.example.vidit.movienary;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
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
    ExternalIdResponse externalIdResponse;
    FloatingActionButton facebook,instagram,twitter,imdb,menuButton,shareButton;
    boolean isFABOpen=false;
    String actorId;
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
        menuButton=findViewById(R.id.menuButton);
        facebook=findViewById(R.id.facebook);
        instagram=findViewById(R.id.instagram);
        twitter=findViewById(R.id.twitter);
        imdb=findViewById(R.id.imdb);
        shareButton=findViewById(R.id.shareButton);

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

        externalIdResponse=new ExternalIdResponse();
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen)
                {
                    showFABMenu();
                }
                else
                {
                    closeFABMenu();
                }
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
        menuButton.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        intent=getIntent();
        actorId=intent.getStringExtra("Id");
        Call<Actor> call=ApiClient.getActorsService().getDetails(actorId);
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

        Call<ExternalIdResponse> call3=ApiClient.getActorsService().getExternalIds(actorId);
        call3.enqueue(new Callback<ExternalIdResponse>() {
            @Override
            public void onResponse(Call<ExternalIdResponse> call, Response<ExternalIdResponse> response) {
                externalIdResponse=response.body();
                if(externalIdResponse==null)
                {
                    menuButton.setEnabled(false);
                }
                else
                {
                    menuButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ExternalIdResponse> call, Throwable t) {

            }
        });

        Call<MovieCreditsResponse> call1=ApiClient.getMoviesService().getMovieCredits(actorId);
        call1.enqueue(new Callback<MovieCreditsResponse>() {
            @Override
            public void onResponse(Call<MovieCreditsResponse> call, Response<MovieCreditsResponse> response) {
                MovieCreditsResponse movieCreditsResponse=response.body();
                ArrayList<Movie> moviesList=movieCreditsResponse.cast;
                movieCredits.addAll(moviesList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieCreditsResponse> call, Throwable t) {

            }
        });
        Call<TvCreditsResponse> call2=ApiClient.getTvService().getTvCredits(actorId);
        call2.enqueue(new Callback<TvCreditsResponse>() {
            @Override
            public void onResponse(Call<TvCreditsResponse> call, Response<TvCreditsResponse> response) {
                TvCreditsResponse tvCreditsResponse=response.body();
                ArrayList<Tv> tvShowsList=tvCreditsResponse.cast;
                tvCredits.addAll(tvShowsList);
                tvAdapter.notifyDataSetChanged();

                actorNameTextView.setVisibility(View.VISIBLE);
                biographyTextView.setVisibility(View.VISIBLE);
                ageTextView.setVisibility(View.VISIBLE);
                birthPlaceTextView.setVisibility(View.VISIBLE);
                biography.setVisibility(View.VISIBLE);
                movieCreditsTextView.setVisibility(View.VISIBLE);
                movieCreditsRecyclerView.setVisibility(View.VISIBLE);
                tvShowCredits.setVisibility(View.VISIBLE);
                profileImageView.setVisibility(View.VISIBLE);
                tvCreditsRecyclerView.setVisibility(View.VISIBLE);
                menuButton.setVisibility(View.VISIBLE);
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
    public void openFacebook(View view)
    {
        if(externalIdResponse.facebookId==null)
        {
            Toast.makeText(this,"Link not available",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"+externalIdResponse.facebookId+"/?ref=br_rs"));
        startActivity(intent);
    }
    public void openTwitter(View view)
    {
        if(externalIdResponse.twitterId==null)
        {
            Toast.makeText(this,"Link not available",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/"+externalIdResponse.twitterId));
        startActivity(intent);
    }
    public void openInstagram(View view)
    {
        if(externalIdResponse.instagramId==null)
        {
            Toast.makeText(this,"Link not available",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/"+externalIdResponse.instagramId));
        startActivity(intent);
    }
    public void openImdb(View view)
    {
        if(externalIdResponse.imdbId==null)
        {
            Toast.makeText(this,"Link not available",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.imdb.com/find?ref_=nv_sr_fn&q="+externalIdResponse.imdbId+"&s=all"));
        startActivity(intent);
    }
    public void shareActor(View view)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"Check out "+actorNameTextView.getText().toString()+" at "+"https://www.themoviedb.org/person/"+actorId);
        intent.setType("text/plain");
        startActivity(intent);
    }
    private void showFABMenu(){
        isFABOpen=true;
        facebook.setVisibility(View.VISIBLE);
        twitter.setVisibility(View.VISIBLE);
        instagram.setVisibility(View.VISIBLE);
        imdb.setVisibility(View.VISIBLE);
        shareButton.setVisibility(View.VISIBLE);

        menuButton.animate().rotationBy(135);
        facebook.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
        twitter.animate().translationY(-getResources().getDimension(R.dimen.standard_135));
        instagram.animate().translationY(-getResources().getDimension(R.dimen.standard_195));
        imdb.animate().translationY(-getResources().getDimension(R.dimen.standard_255));
        shareButton.animate().translationX(-getResources().getDimension(R.dimen.standard_75));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        menuButton.animate().rotationBy(-135);
        facebook.animate().translationY(0);
        twitter.animate().translationY(0);
        instagram.animate().translationY(0);
        imdb.animate().translationY(0);
        shareButton.animate().translationX(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen)
                {
                    facebook.setVisibility(View.GONE);
                    twitter.setVisibility(View.GONE);
                    instagram.setVisibility(View.GONE);
                    imdb.setVisibility(View.GONE);
                    shareButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

}
