package com.example.vidit.movienary;

import android.animation.Animator;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailsActivity extends AppCompatActivity
{
    TextView titleTextView,plot,writerTextView,totalSeasonsTextView,awardsTextView,ratingTextView,imdbRatingTextView,tmdbRatingTextView,rtRatingTextView,metaRatingTextView,genreTextView,castTextView,firstAirDateTextView,authorTextView,reviewsTextView,similarTvShowsTextView,videoTextView;
    ExpandableTextView overviewTextView,bodyTextView,plotTextView;
    ImageView backdropImageView,tmdbImageView,imdbImageView,rtImageView,metaImageView;
    RecyclerView castRecyclerView,similarTvShowsRecyclerView,videoRecyclerView;
    CastAdapter adapter;
    TvAdapter tvAdapter;
    VideoAdapter videoAdapter;
    Intent intent1,intent;
    ArrayList<Cast> actorsList=new ArrayList<>();
    ArrayList<Review> reviewsList=new ArrayList<>();
    ArrayList<Tv> similarTvShowsList=new ArrayList<>();
    ArrayList<Video> videoArrayList=new ArrayList<>();
    ArrayList<Tv> watchlistTvShows=new ArrayList<>();
    LottieAnimationView loading;
    Button readAllReviewsButton;
    ExternalIdResponse externalIdResponse;
    FloatingActionButton watchlistButton;
    android.support.v7.widget.Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    OmdbResponse omdbResponse;
    boolean watchlistButtonClicked;
    String tvShowName;
    String posterPath;
    String backdropPath;
    String tvShowId;
    String rating;
    String overview;
    String firstAirDate;
    FloatingActionButton facebook,instagram,twitter,imdb,menuButton,shareButton;
    boolean isFABOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);
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
        ratingTextView=findViewById(R.id.ratingTextView);
        tmdbRatingTextView = findViewById(R.id.tmdbRatingTextView);
        imdbRatingTextView=findViewById(R.id.imdbRatingTextView);
        rtRatingTextView=findViewById(R.id.rtRatingTextView);
        metaRatingTextView=findViewById(R.id.metaRatingTextView);
        backdropImageView = findViewById(R.id.backdropImageView);
        loading=findViewById(R.id.loading);
        genreTextView=findViewById(R.id.genreTextView);
        tmdbImageView = findViewById(R.id.tmdbImageView);
        imdbImageView=findViewById(R.id.imdbImageView);
        rtImageView=findViewById(R.id.rtImageView);
        metaImageView=findViewById(R.id.metaImageView);
        castRecyclerView=findViewById(R.id.castRecyclerView);
        castTextView=findViewById(R.id.castTextView);
        authorTextView=findViewById(R.id.authorTextView);
        bodyTextView=findViewById(R.id.reviewBodyTextView);
        firstAirDateTextView=findViewById(R.id.firstAirDateTextView);
        similarTvShowsRecyclerView=findViewById(R.id.similarTvShowsRecyclerView);
        similarTvShowsTextView=findViewById(R.id.similarTvShowsTextView);
        videoRecyclerView=findViewById(R.id.videoRecyclerView);
        videoTextView=findViewById(R.id.videoTextView);
        watchlistButton=findViewById(R.id.watchlistButton);
        menuButton=findViewById(R.id.menuButton);
        facebook=findViewById(R.id.facebook);
        instagram=findViewById(R.id.instagram);
        twitter=findViewById(R.id.twitter);
        imdb=findViewById(R.id.imdb);
        shareButton=findViewById(R.id.shareButton);
        plot=findViewById(R.id.plot);
        plotTextView=findViewById(R.id.plotTextView);
        writerTextView=findViewById(R.id.writerTextView);
        totalSeasonsTextView=findViewById(R.id.totalSeasonsTextView);
        awardsTextView=findViewById(R.id.awardsTextView);
        externalIdResponse=new ExternalIdResponse();
        watchlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToWatchlist(view);
            }
        });

        adapter=new CastAdapter(TvShowDetailsActivity.this, actorsList, new CastClickListener() {
            @Override
            public void onActorClick(View view, int position) {
                Cast cast=actorsList.get(position);
                String id=cast.id;
                intent1=new Intent(TvShowDetailsActivity.this,ActorDetailsActivity.class);
                intent1.putExtra("Id",id);
                startActivity(intent1);
            }
        });

        tvAdapter=new TvAdapter(TvShowDetailsActivity.this, similarTvShowsList, new TvClickListener() {
            @Override
            public void onTvClick(View view, int position) {
                Tv tvShow=similarTvShowsList.get(position);
                Bundle bundle=new Bundle();
                bundle.putString("TvShowName",tvShow.tvShowName);
                bundle.putString("PosterPath",tvShow.posterPath);
                bundle.putString("Rating",tvShow.rating);
                bundle.putString("Id",tvShow.id);
                bundle.putString("BackdropPath",tvShow.backdropPath);
                bundle.putString("Overview",tvShow.overview);
                bundle.putString("FirstAirDate",tvShow.firstAirDate);
                Intent intent=new Intent(TvShowDetailsActivity.this,TvShowDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        videoAdapter=new VideoAdapter(TvShowDetailsActivity.this, videoArrayList, new VideoClickListener() {
            @Override
            public void onVideoClick(View view, int position) {
                Video video=videoArrayList.get(position);
                //Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+video.key));
                Intent intent=new Intent(TvShowDetailsActivity.this,YoutubeActivity.class);
                intent.putExtra("Key",video.key);
                startActivity(intent);
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
        LinearLayoutManager layoutManager=new LinearLayoutManager(TvShowDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        castRecyclerView.setLayoutManager(layoutManager);
        castRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager1=new LinearLayoutManager(TvShowDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        similarTvShowsRecyclerView.setLayoutManager(layoutManager1);
        similarTvShowsRecyclerView.setAdapter(tvAdapter);

        LinearLayoutManager layoutManager2=new LinearLayoutManager(TvShowDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        videoRecyclerView.setLayoutManager(layoutManager2);
        videoRecyclerView.setAdapter(videoAdapter);

        intent=getIntent();

        Bundle bundle=intent.getExtras();
        tvShowName = bundle.getString("TvShowName");
        posterPath = bundle.getString("PosterPath");
        rating = bundle.getString("Rating");
        tvShowId = bundle.getString("Id");
        backdropPath = bundle.getString("BackdropPath");
        overview = bundle.getString("Overview");
        firstAirDate = bundle.getString("FirstAirDate");

        WatchlistOpenHelper openHelper=WatchlistOpenHelper.getInstance(TvShowDetailsActivity.this);
        SQLiteDatabase database=openHelper.getReadableDatabase();
        String[] selectionArgs={tvShowId+""};
        String[] columns={ContractTv.Tv.COLUMN_TVSHOWNAME};
        Cursor cursor=database.query(ContractTv.Tv.TABLE_NAME,columns,ContractTv.Tv.COLUMN_ID+" =?",selectionArgs,null,null,null);
        String name=null;
        while(cursor.moveToNext())
        {
            name=cursor.getString(cursor.getColumnIndex(ContractTv.Tv.COLUMN_TVSHOWNAME));
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

        collapsingToolbarLayout.setTitle(tvShowName);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        titleTextView.setVisibility(View.GONE);
        backdropImageView.setVisibility(View.GONE);
        overviewTextView.setVisibility(View.GONE);
        ratingTextView.setVisibility(View.GONE);
        tmdbImageView.setVisibility(View.GONE);
        tmdbRatingTextView.setVisibility(View.GONE);
        imdbImageView.setVisibility(View.GONE);
        imdbRatingTextView.setVisibility(View.GONE);
        rtImageView.setVisibility(View.GONE);
        rtRatingTextView.setVisibility(View.GONE);
        metaImageView.setVisibility(View.GONE);
        metaRatingTextView.setVisibility(View.GONE);
        genreTextView.setVisibility(View.GONE);
        castTextView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        castRecyclerView.setVisibility(View.GONE);
        firstAirDateTextView.setVisibility(View.GONE);
        reviewsTextView.setVisibility(View.GONE);
        authorTextView.setVisibility(View.GONE);
        bodyTextView.setVisibility(View.GONE);
        readAllReviewsButton.setVisibility(View.GONE);
        similarTvShowsRecyclerView.setVisibility(View.GONE);
        similarTvShowsTextView.setVisibility(View.GONE);
        videoRecyclerView.setVisibility(View.GONE);
        videoTextView.setVisibility(View.GONE);
        watchlistButton.setVisibility(View.GONE);
        menuButton.setVisibility(View.GONE);
        plot.setVisibility(View.GONE);
        plotTextView.setVisibility(View.GONE);
        writerTextView.setVisibility(View.GONE);
        totalSeasonsTextView.setVisibility(View.GONE);
        awardsTextView.setVisibility(View.GONE);
        titleTextView.setText(tvShowName);

        backdropImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=view.getId();
                if(id==R.id.backdropImageView)
                {
                    if(backdropPath==null)
                    {
                        Toast.makeText(TvShowDetailsActivity.this,"This image is not available",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent=new Intent(TvShowDetailsActivity.this,ImageActivity.class);
                    intent.putExtra("BackdropPath",backdropPath);
                    startActivity(intent);
                }
            }
        });

        Picasso.get().load("http://image.tmdb.org/t/p/original//" + backdropPath).resize(1100, 618).into(backdropImageView);
        overviewTextView.setText(overview);
        if(Float.parseFloat(rating)==0)
        {
            String r="<b>"+"N/A"+"</b>";
            tmdbRatingTextView.setText(Html.fromHtml(r));
        }
        else
        {
            String r="<b>"+rating+"/10"+"</b>";
            tmdbRatingTextView.setText(Html.fromHtml(r));
        }
        if(firstAirDate!=null)
        {
            String fd[]=firstAirDate.split("-");
            String year=fd[0];
            String month=fd[1];
            String day=fd[2];
            firstAirDateTextView.setText("First Air Date: "+day+"/"+month+"/"+year);
        }
        else
        {
            firstAirDateTextView.setText("First Air Date: "+firstAirDate);
        }

        Call<SingleMovie> call=ApiClient.getTvService().getDetails(tvShowId);
        call.enqueue(new Callback<SingleMovie>() {
            @Override
            public void onResponse(Call<SingleMovie> call, Response<SingleMovie> response)
            {
                SingleMovie singleMovie=response.body();
                ArrayList<Genre> genre=singleMovie.genres;
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

        Call<ExternalIdResponse> call5=ApiClient.getTvService().getExternalIds(tvShowId);
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

        Call<CastResponse> call1=ApiClient.getTvService().getCast(tvShowId);
        call1.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call1, Response<CastResponse> response) {
                CastResponse castResponse=response.body();
                ArrayList<Cast> castList=castResponse.cast;
                actorsList.clear();
                actorsList.addAll(castList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CastResponse> call1, Throwable t) {
            }
        });

        Call<VideoResponse> call4=ApiClient.getVideoService().getTvShowVideos(tvShowId);
        call4.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                VideoResponse videoResponse=response.body();
                ArrayList<Video> videos=videoResponse.results;
                videoArrayList.addAll(videos);
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });

        Call<OmdbResponse> call6=ApiClient.getTvService().getOmdbDetails(tvShowName);
        call6.enqueue(new Callback<OmdbResponse>() {
            @Override
            public void onResponse(Call<OmdbResponse> call, Response<OmdbResponse> response) {
                omdbResponse=response.body();
                ArrayList<Ratings> ratings=omdbResponse.ratings;
                String flag=omdbResponse.response;
                if(flag.equals("False"))
                {
                    imdbRatingTextView.setText("N/A");
                    rtRatingTextView.setText("N/A");
                    metaRatingTextView.setText("N/A");
                    writerTextView.setText(writerTextView.getText()+"N/A");
                    totalSeasonsTextView.setText(totalSeasonsTextView.getText()+"N/A");
                    awardsTextView.setText(awardsTextView.getText()+"N/A");
                    plotTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                    plotTextView.setTextSize(15);
                    String b="<b>"+"Not Available"+"</b>";
                    plotTextView.setText(Html.fromHtml(b));
                    return;
                }
                imdbRatingTextView.setText("N/A");
                rtRatingTextView.setText("N/A");
                metaRatingTextView.setText("N/A");
                for(int i=0;i<ratings.size();i++)
                {
                    if(ratings.get(i).source.equals("Internet Movie Database"))
                    {
                        imdbRatingTextView.setText(ratings.get(i).value);
                    }
                    else if(ratings.get(i).source.equals("Rotten Tomatoes"))
                    {
                        rtRatingTextView.setText(ratings.get(i).value);
                    }
                    else if(ratings.get(i).source.equals("Metacritic"))
                    {
                        metaRatingTextView.setText(ratings.get(i).value);
                    }
                }
                plotTextView.setText(omdbResponse.plot);
                String w=omdbResponse.writer;
                String a=omdbResponse.awards;
                String t=omdbResponse.totalSeasons;
                for(int i=0;i<a.length();i++)
                {
                    if(a.charAt(i)=='.' && i!=a.length()-1)
                    {
                        String s1=a.substring(0,i);
                        String s2=a.substring(i+1,a.length());
                        a=s1+", "+s2;
                    }
                    if(a.charAt(i)=='.' && i==a.length()-1)
                    {
                        a=a.substring(0,a.length()-1);
                    }
                }
                if(w==null)
                {
                    writerTextView.setText("Writer: N/A");
                }
                else
                {
                    writerTextView.setText("Writer: "+w);
                }
                if(a==null)
                {
                    awardsTextView.setText("Awards: N/A");
                }
                else
                {
                    awardsTextView.setText("Awards: "+a);
                }
                if(t==null)
                {
                    totalSeasonsTextView.setText("Total Seasons: N/A");
                }
                else
                {
                    totalSeasonsTextView.setText("Total Seasons: "+t);
                }
            }

            @Override
            public void onFailure(Call<OmdbResponse> call, Throwable t) {

            }
        });

        Call<TvResponse> call3=ApiClient.getTvService().getSimilarTvShows(tvShowId);
        call3.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                TvResponse tvResponse=response.body();
                ArrayList<Tv> tvShows=tvResponse.results;
                similarTvShowsList.clear();
                similarTvShowsList.addAll(tvShows);
                tvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {

            }
        });

        Call<ReviewResponse> call2=ApiClient.getTvService().getReviews(tvShowId);
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
                tmdbImageView.setVisibility(View.VISIBLE);
                tmdbRatingTextView.setVisibility(View.VISIBLE);
                imdbImageView.setVisibility(View.VISIBLE);
                imdbRatingTextView.setVisibility(View.VISIBLE);
                rtImageView.setVisibility(View.VISIBLE);
                rtRatingTextView.setVisibility(View.VISIBLE);
                metaImageView.setVisibility(View.VISIBLE);
                metaRatingTextView.setVisibility(View.VISIBLE);
                ratingTextView.setVisibility(View.VISIBLE);
                readAllReviewsButton.setVisibility(View.VISIBLE);
                genreTextView.setVisibility(View.VISIBLE);
                castTextView.setVisibility(View.VISIBLE);
                firstAirDateTextView.setVisibility(View.VISIBLE);
                authorTextView.setVisibility(View.VISIBLE);
                bodyTextView.setVisibility(View.VISIBLE);
                reviewsTextView.setVisibility(View.VISIBLE);
                similarTvShowsRecyclerView.setVisibility(View.VISIBLE);
                similarTvShowsTextView.setVisibility(View.VISIBLE);
                videoRecyclerView.setVisibility(View.VISIBLE);
                videoTextView.setVisibility(View.VISIBLE);
                watchlistButton.setVisibility(View.VISIBLE);
                menuButton.setVisibility(View.VISIBLE);
                castRecyclerView.setVisibility(View.VISIBLE);
                plot.setVisibility(View.VISIBLE);
                plotTextView.setVisibility(View.VISIBLE);
                writerTextView.setVisibility(View.VISIBLE);
                awardsTextView.setVisibility(View.VISIBLE);
                totalSeasonsTextView.setVisibility(View.VISIBLE);
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
            Intent intent=new Intent(TvShowDetailsActivity.this,ReviewsActivity.class);
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
        Tv tvShow=new Tv();
        tvShow.tvShowName=tvShowName;
        tvShow.posterPath=posterPath;
        tvShow.rating=rating;
        tvShow.id=tvShowId;
        tvShow.backdropPath=backdropPath;
        tvShow.overview=overview;
        tvShow.firstAirDate=firstAirDate;
        watchlistTvShows.add(tvShow);
        watchlistButtonClicked=true;
        WatchlistOpenHelper openHelper=WatchlistOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database=openHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ContractTv.Tv.COLUMN_ID,tvShowId);
        contentValues.put(ContractTv.Tv.COLUMN_TVSHOWNAME,tvShowName);
        contentValues.put(ContractTv.Tv.COLUMN_OVERVIEW,overview);
        contentValues.put(ContractTv.Tv.COLUMN_RATING,rating);
        contentValues.put(ContractTv.Tv.COLUMN_FIRSTAIRDATE,firstAirDate);
        contentValues.put(ContractTv.Tv.COLUMN_POSTERPATH,posterPath);
        contentValues.put(ContractTv.Tv.COLUMN_BACKDROPPATH,backdropPath);
        database.insert(ContractTv.Tv.TABLE_NAME,null,contentValues);
    }
    public void watchlistRemove()
    {
        watchlistButton.setImageResource(R.mipmap.bookmark);
        watchlistButtonClicked=false;
        WatchlistOpenHelper openHelper=WatchlistOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database=openHelper.getWritableDatabase();
        String[] selectionArgs={tvShowId+""};
        database.delete(ContractTv.Tv.TABLE_NAME,ContractTv.Tv.COLUMN_ID+" =?",selectionArgs);
        for(int i=0;i<watchlistTvShows.size();i++)
        {
            Tv tvShow=watchlistTvShows.get(i);
            if(tvShow.id.equals(tvShowId))
            {
                watchlistTvShows.remove(i);
            }
        }
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
    public void shareTvShow(View view)
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"Check out "+tvShowName+" at "+"https://www.themoviedb.org/tv/"+tvShowId);
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
