package com.example.vidit.movienary;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.solver.GoalRow;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity
{
    TextView titleTextView,ratingTextView,runTimeTextView,genreTextView,castTextView,reviewsTextView,releaseDateTextView,authorTextView,similarMoviesTextView,videoTextView;
    ExpandableTextView overviewTextView,bodyTextView;
    ImageView backdropImageView,starImageView;
    Intent intent,intent1;
    RecyclerView castRecyclerView,similarMoviesRecyclerView,videoRecyclerView;
    CastAdapter adapter;
    VideoAdapter videoAdapter;
    MovieAdapter movieAdapter;
    ArrayList<Cast> actorsList=new ArrayList<>();
    ArrayList<Review> reviewsList=new ArrayList<>();
    ArrayList<Movie> similarMoviesList=new ArrayList<>();
    ArrayList<Video> videoArrayList=new ArrayList<>();
    LottieAnimationView loading;
    Button readAllReviewsButton;
    android.support.v7.widget.Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        titleTextView = findViewById(R.id.titleTextView);

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

        overviewTextView = findViewById(R.id.overviewTextView);
        reviewsTextView=findViewById(R.id.reviewsTextView);
        readAllReviewsButton=findViewById(R.id.readAllReviewsButton);
        ratingTextView = findViewById(R.id.ratingTextView);
        backdropImageView = findViewById(R.id.backdropImageView);
        loading=findViewById(R.id.loading);
        genreTextView=findViewById(R.id.genreTextView);
        starImageView = findViewById(R.id.starImageView);
        runTimeTextView=findViewById(R.id.runTimeTextView);
        castRecyclerView=findViewById(R.id.castRecyclerView);
        castTextView=findViewById(R.id.castTextView);
        authorTextView=findViewById(R.id.authorTextView);
        bodyTextView=findViewById(R.id.reviewBodyTextView);
        releaseDateTextView=findViewById(R.id.releaseDateTextView);
        similarMoviesTextView=findViewById(R.id.similarMoviesTextView);
        similarMoviesRecyclerView=findViewById(R.id.similarMoviesRecyclerView);
        videoRecyclerView=findViewById(R.id.videoRecyclerView);
        videoTextView=findViewById(R.id.videoTextView);

        adapter=new CastAdapter(DetailsActivity.this, actorsList, new CastClickListener() {
            @Override
            public void onActorClick(View view, int position) {
                Cast cast=actorsList.get(position);
                String id=cast.id;
                intent1=new Intent(DetailsActivity.this,ActorDetailsActivity.class);
                intent1.putExtra("Id",id);
                startActivity(intent1);
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(DetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        castRecyclerView.setLayoutManager(layoutManager);
        castRecyclerView.setAdapter(adapter);

        movieAdapter=new MovieAdapter(DetailsActivity.this, similarMoviesList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                Movie movie=similarMoviesList.get(position);
                Bundle bundle=new Bundle();
                bundle.putString("MovieName",movie.movieName);
                bundle.putString("PosterPath",movie.posterPath);
                bundle.putString("Rating",movie.rating);
                bundle.putString("Id",movie.id);
                bundle.putString("BackdropPath",movie.backdropPath);
                bundle.putString("Overview",movie.overview);
                bundle.putString("ReleaseDate",movie.releaseDate);
                Intent intent=new Intent(DetailsActivity.this,DetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager1=new LinearLayoutManager(DetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        similarMoviesRecyclerView.setLayoutManager(layoutManager1);
        similarMoviesRecyclerView.setAdapter(movieAdapter);

        videoAdapter=new VideoAdapter(DetailsActivity.this, videoArrayList, new VideoClickListener() {
            @Override
            public void onVideoClick(View view, int position) {
                Video video=videoArrayList.get(position);
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+video.key));
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager2=new LinearLayoutManager(DetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        videoRecyclerView.setLayoutManager(layoutManager2);
        videoRecyclerView.setAdapter(videoAdapter);


        intent = getIntent();

        Bundle bundle=intent.getExtras();
        String movieName = bundle.getString("MovieName");
        String posterPath = bundle.getString("PosterPath");
        String rating = bundle.getString("Rating");
        String id = bundle.getString("Id");
        final String backdropPath = bundle.getString("BackdropPath");
        String overview = bundle.getString("Overview");
        final String releaseDate = bundle.getString("ReleaseDate");

        collapsingToolbarLayout.setTitle(movieName);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        titleTextView.setVisibility(View.GONE);
        backdropImageView.setVisibility(View.GONE);
        overviewTextView.setVisibility(View.GONE);
        starImageView.setVisibility(View.GONE);
        ratingTextView.setVisibility(View.GONE);
        runTimeTextView.setVisibility(View.GONE);
        genreTextView.setVisibility(View.GONE);
        castTextView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        castRecyclerView.setVisibility(View.GONE);
        releaseDateTextView.setVisibility(View.GONE);
        reviewsTextView.setVisibility(View.GONE);
        authorTextView.setVisibility(View.GONE);
        bodyTextView.setVisibility(View.GONE);
        readAllReviewsButton.setVisibility(View.GONE);
        similarMoviesTextView.setVisibility(View.GONE);
        similarMoviesRecyclerView.setVisibility(View.GONE);
        videoRecyclerView.setVisibility(View.GONE);
        videoTextView.setVisibility(View.GONE);
        titleTextView.setText(movieName);

        backdropImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=view.getId();
                if(id==R.id.backdropImageView)
                {
                    Intent intent=new Intent(DetailsActivity.this,ImageActivity.class);
                    intent.putExtra("BackdropPath",backdropPath);
                    startActivity(intent);
                }
            }
        });

        Picasso.get().load("http://image.tmdb.org/t/p/original//" + backdropPath).resize(1100, 618).into(backdropImageView);
        overviewTextView.setText(overview);
        Picasso.get().load("https://cdn2.iconfinder.com/data/icons/modifiers-add-on-1-flat/48/Mod_Add-On_1-35-512.png").into(starImageView);
        if(Float.parseFloat(rating)==0)
        {
            String r="<b>"+"N/A"+"</b>";
            ratingTextView.setText(Html.fromHtml(r));
        }
        else
        {
            String r="<b>"+rating+"</b>";
            ratingTextView.setText(Html.fromHtml(r));
        }
        releaseDateTextView.setText("Release Date: "+releaseDate);

        Call<SingleMovie> call=ApiClient.getMoviesService().getDetails(id);
        call.enqueue(new Callback<SingleMovie>() {
            @Override
            public void onResponse(Call<SingleMovie> call, Response<SingleMovie> response)
            {
                SingleMovie singleMovie=response.body();
                int runtime=singleMovie.runtime;
                ArrayList<Genre> genre=singleMovie.genres;
                int hours=runtime/60;
                int minutes=runtime%60;
                String time=hours+" h "+minutes+" m";
                runTimeTextView.setText("Runtime: "+time);
                String genreString="";
                for(int i=0;i<genre.size();i++)
                {
                    Genre g=genre.get(i);
                    if(genreString.length()==0)
                    {
                        genreString=g.name;
                    }
                    else
                    {
                        genreString=genreString+", "+g.name;
                    }
                }
                genreTextView.setText("Genre: "+genreString);
            }

            @Override
            public void onFailure(Call<SingleMovie> call, Throwable t) {

            }
        });

        Call<VideoResponse> call4=ApiClient.getVideoService().getVideos(id);
        call4.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                VideoResponse videoResponse=response.body();
                ArrayList<Video> videos=videoResponse.results;
                videoArrayList.addAll(videos);
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });

        Call<CastResponse> call1=ApiClient.getMoviesService().getCast(id);
        call1.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call1, Response<CastResponse> response) {
                CastResponse castResponse=response.body();
                ArrayList<Cast> castList=castResponse.cast;
                actorsList.clear();
                actorsList.addAll(castList);
            }

            @Override
            public void onFailure(Call<CastResponse> call1, Throwable t) {
            }
        });

        Call<MovieResponse> call3=ApiClient.getMoviesService().getSimilarMovies(id);
        call3.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call3, Response<MovieResponse> response)
            {
                MovieResponse movieResponse=response.body();
                ArrayList<Movie> movies=movieResponse.results;
                similarMoviesList.clear();
                similarMoviesList.addAll(movies);
            }

            @Override
            public void onFailure(Call<MovieResponse> call3, Throwable t) {

            }
        });

        Call<ReviewResponse> call2=ApiClient.getMoviesService().getReviews(id);
        call2.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call2, Response<ReviewResponse> response)
            {
                ReviewResponse reviewResponse=response.body();
                ArrayList<Review> reviews=reviewResponse.results;
                reviewsList.clear();
                reviewsList.addAll(reviews);
                reviewsTextView.setText(reviewsTextView.getText()+" "+"("+reviewsList.size()+")");
                if(reviewsList.size()==0)
                {
                    authorTextView.setText("No Reviews Available");
                    bodyTextView.setText("");
                    readAllReviewsButton.setEnabled(false);
                    readAllReviewsButton.setTextColor(Color.parseColor("#d3d3d3"));
                }
                else if(reviewsList.size()==1)
                {
                    authorTextView.setText(reviewsList.get(0).author);
                    bodyTextView.setText(reviewsList.get(0).content);
                    readAllReviewsButton.setEnabled(false);
                    readAllReviewsButton.setTextColor(Color.parseColor("#d3d3d3"));
                }
                else
                {
                    authorTextView.setText(reviewsList.get(0).author);
                    bodyTextView.setText(reviewsList.get(0).content);
                }
                titleTextView.setVisibility(View.VISIBLE);
                backdropImageView.setVisibility(View.VISIBLE);
                overviewTextView.setVisibility(View.VISIBLE);
                starImageView.setVisibility(View.VISIBLE);
                ratingTextView.setVisibility(View.VISIBLE);
                runTimeTextView.setVisibility(View.VISIBLE);
                readAllReviewsButton.setVisibility(View.VISIBLE);
                genreTextView.setVisibility(View.VISIBLE);
                castTextView.setVisibility(View.VISIBLE);
                castRecyclerView.setVisibility(View.VISIBLE);
                releaseDateTextView.setVisibility(View.VISIBLE);
                authorTextView.setVisibility(View.VISIBLE);
                bodyTextView.setVisibility(View.VISIBLE);
                reviewsTextView.setVisibility(View.VISIBLE);
                similarMoviesTextView.setVisibility(View.VISIBLE);
                similarMoviesRecyclerView.setVisibility(View.VISIBLE);
                videoRecyclerView.setVisibility(View.VISIBLE);
                videoTextView.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ReviewResponse> call2, Throwable t) {

            }
        });
    }
    public void readAllReviews(View view)
    {
        int id=view.getId();
        if(id==R.id.readAllReviewsButton)
        {
            ArrayList<String> authors=new ArrayList<>();
            ArrayList<String> content=new ArrayList<>();
            for(int i=0;i<reviewsList.size();i++)
            {
                authors.add(reviewsList.get(i).author);
                content.add(reviewsList.get(i).content);
            }
            Intent intent=new Intent(DetailsActivity.this,ReviewsActivity.class);
            intent.putExtra("authors",authors);
            intent.putExtra("content",content);
            startActivity(intent);
        }
    }
}