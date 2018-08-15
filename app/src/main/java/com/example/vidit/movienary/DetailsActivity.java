package com.example.vidit.movienary;

import android.animation.Animator;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.solver.GoalRow;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.github.clans.fab.FloatingActionMenu;
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
    ExternalIdResponse externalIdResponse;
    //ImageButton watchlistButton;
    FloatingActionButton watchlistButton;
    ArrayList<Movie> watchlistMovies=new ArrayList<>();
    android.support.v7.widget.Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    boolean watchlistButtonClicked;
    String movieName;
    String posterPath;
    String backdropPath;
    String movieId;
    String rating;
    String overview;
    String releaseDate;
    FloatingActionButton facebook,instagram,twitter,imdb,menuButton,shareButton;
    boolean isFABOpen=false;
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
        watchlistButton=findViewById(R.id.watchlistButton);
        menuButton=findViewById(R.id.menuButton);
        facebook=findViewById(R.id.facebook);
        instagram=findViewById(R.id.instagram);
        twitter=findViewById(R.id.twitter);
        imdb=findViewById(R.id.imdb);
        shareButton=findViewById(R.id.shareButton);
        externalIdResponse=new ExternalIdResponse();

        watchlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToWatchlist(view);
            }
        });

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
                //Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+video.key));
                Intent intent=new Intent(DetailsActivity.this,YoutubeActivity.class);
                intent.putExtra("Key",video.key);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager2=new LinearLayoutManager(DetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        videoRecyclerView.setLayoutManager(layoutManager2);
        videoRecyclerView.setAdapter(videoAdapter);


        intent = getIntent();

        Bundle bundle=intent.getExtras();
        movieName = bundle.getString("MovieName");
        posterPath = bundle.getString("PosterPath");
        rating = bundle.getString("Rating");
        movieId = bundle.getString("Id");
        backdropPath = bundle.getString("BackdropPath");
        overview = bundle.getString("Overview");
        releaseDate = bundle.getString("ReleaseDate");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            String transitionName=bundle.getString("Transition");
            backdropImageView.setTransitionName(transitionName);
        }

        WatchlistOpenHelper openHelper=WatchlistOpenHelper.getInstance(DetailsActivity.this);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        String[] selectionArgs={movieId+""};
        String[] columns={ContractMovies.Movie.COLUMN_MOVIENAME};
        Cursor cursor=database.query(ContractMovies.Movie.TABLE_NAME,columns,ContractMovies.Movie.COLUMN_ID+" =?",selectionArgs,null,null,null);
        String name=null;
        while(cursor.moveToNext())
        {
            name=cursor.getString(cursor.getColumnIndex(ContractMovies.Movie.COLUMN_MOVIENAME));
        }
        if(name==null)
        {
            watchlistButton.setImageResource(R.mipmap.bookmark);
            watchlistButtonClicked=false;
        }
        else if(name!=null)
        {
            watchlistButton.setImageResource(R.mipmap.bookmark_fill);
            watchlistButtonClicked=true;
        }
        //Log.d("movieName",cursor.getString(0));
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
        watchlistButton.setVisibility(View.GONE);
        titleTextView.setText(movieName);
        menuButton.setVisibility(View.GONE);

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
        if(releaseDate!=null)
        {
            String[] rd=releaseDate.split("-");
            String year=rd[0];
            String month=rd[1];
            String day=rd[2];
            releaseDateTextView.setText("Release Date: "+day+"/"+month+"/"+year);
        }
        else
        {
            releaseDateTextView.setText("Release Date: "+releaseDate);
        }
        Call<SingleMovie> call=ApiClient.getMoviesService().getDetails(movieId);
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

        Call<VideoResponse> call4=ApiClient.getVideoService().getVideos(movieId);
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

        Call<CastResponse> call1=ApiClient.getMoviesService().getCast(movieId);
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

        Call<MovieResponse> call3=ApiClient.getMoviesService().getSimilarMovies(movieId);
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

        Call<ExternalIdResponse> call5=ApiClient.getMoviesService().getExternalIds(movieId);
        call5.enqueue(new Callback<ExternalIdResponse>() {
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

        Call<ReviewResponse> call2=ApiClient.getMoviesService().getReviews(movieId);
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
                watchlistButton.setVisibility(View.VISIBLE);
                menuButton.setVisibility(View.VISIBLE);
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

    public void addToWatchlist(View view)
    {
        final Snackbar removeSnackBar=Snackbar.make(view,"Removed from Watchlist",Snackbar.LENGTH_LONG).setActionTextColor(Color.parseColor("#FFDE03"));
        View rView=removeSnackBar.getView();
        rView.setBackgroundColor(Color.BLACK);
        final Snackbar addSnackBar=Snackbar.make(view,"Added to Watchlist",Snackbar.LENGTH_LONG).setActionTextColor(Color.parseColor("#FFDE03"));
        View aView=addSnackBar.getView();
        aView.setBackgroundColor(Color.BLACK);
        removeSnackBar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchlistAdd();
                removeSnackBar.dismiss();
                addSnackBar.show();
            }
        });

        addSnackBar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchlistRemove();
                addSnackBar.dismiss();
                removeSnackBar.show();
            }
        });

        int id=view.getId();
        if(id==R.id.watchlistButton)
        {
            if(watchlistButtonClicked==true)
            {
                watchlistRemove();
                removeSnackBar.show();
            }
            else if(watchlistButtonClicked==false)
            {
                watchlistAdd();
                addSnackBar.show();
            }
        }
    }
    public void watchlistAdd()
    {
        watchlistButton.setImageResource(R.mipmap.bookmark_fill);
        Movie movie=new Movie();
        movie.movieName=movieName;
        movie.posterPath=posterPath;
        movie.rating=rating;
        movie.id=movieId;
        movie.backdropPath=backdropPath;
        movie.overview=overview;
        movie.releaseDate=releaseDate;
        watchlistMovies.add(movie);
        watchlistButtonClicked=true;
        WatchlistOpenHelper openHelper=WatchlistOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database=openHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ContractMovies.Movie.COLUMN_ID,movieId);
        contentValues.put(ContractMovies.Movie.COLUMN_MOVIENAME,movieName);
        contentValues.put(ContractMovies.Movie.COLUMN_OVERVIEW,overview);
        contentValues.put(ContractMovies.Movie.COLUMN_RATING,rating);
        contentValues.put(ContractMovies.Movie.COLUMN_RELEASEDATE,releaseDate);
        contentValues.put(ContractMovies.Movie.COLUMN_POSTERPATH,posterPath);
        contentValues.put(ContractMovies.Movie.COLUMN_BACKDROPPATH,backdropPath);
        database.insert(ContractMovies.Movie.TABLE_NAME,null,contentValues);
    }
    public void watchlistRemove()
    {
        watchlistButton.setImageResource(R.mipmap.bookmark);
        watchlistButtonClicked=false;
        WatchlistOpenHelper openHelper=WatchlistOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database=openHelper.getWritableDatabase();
        String[] selectionArgs={movieId+""};
        database.delete(ContractMovies.Movie.TABLE_NAME,ContractMovies.Movie.COLUMN_ID+" =?",selectionArgs);
        for(int i=0;i<watchlistMovies.size();i++)
        {
            Movie movie=watchlistMovies.get(i);
            if(movie.id.equals(movieId))
            {
                watchlistMovies.remove(i);
            }
        }
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
    public void openFacebook(View view)
    {
        if(externalIdResponse.facebookId==null)
        {
            Toast.makeText(this,"Link not available",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/"+externalIdResponse.facebookId+"/?ref=br_rs"));
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
    public void shareMovie(View view)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"Check out "+movieName+" at "+"https://www.themoviedb.org/movie/"+movieId);
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

        menuButton.animate().rotationBy(180);
        facebook.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
        twitter.animate().translationY(-getResources().getDimension(R.dimen.standard_135));
        instagram.animate().translationY(-getResources().getDimension(R.dimen.standard_195));
        imdb.animate().translationY(-getResources().getDimension(R.dimen.standard_255));
        shareButton.animate().translationX(-getResources().getDimension(R.dimen.standard_75));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        menuButton.animate().rotationBy(-180);
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