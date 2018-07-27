package com.example.vidit.movienary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
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
    TextView titleTextView,ratingTextView,genreTextView,castTextView,firstAirDateTextView,authorTextView,reviewsTextView,similarTvShowsTextView,videoTextView;
    ExpandableTextView overviewTextView,bodyTextView ;
    ImageView backdropImageView,starImageView;
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
    ImageButton watchlistButton;
    android.support.v7.widget.Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    boolean watchlistButtonClicked;
    String tvShowName;
    String posterPath;
    String backdropPath;
    String tvShowId;
    String rating;
    String overview;
    String firstAirDate;
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
        ratingTextView = findViewById(R.id.ratingTextView);
        backdropImageView = findViewById(R.id.backdropImageView);
        loading=findViewById(R.id.loading);
        genreTextView=findViewById(R.id.genreTextView);
        starImageView = findViewById(R.id.starImageView);
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
        LinearLayoutManager layoutManager=new LinearLayoutManager(TvShowDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        castRecyclerView.setLayoutManager(layoutManager);
        castRecyclerView.setAdapter(adapter);

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
        LinearLayoutManager layoutManager1=new LinearLayoutManager(TvShowDetailsActivity.this,LinearLayoutManager.HORIZONTAL,false);
        similarTvShowsRecyclerView.setLayoutManager(layoutManager1);
        similarTvShowsRecyclerView.setAdapter(tvAdapter);

        videoAdapter=new VideoAdapter(TvShowDetailsActivity.this, videoArrayList, new VideoClickListener() {
            @Override
            public void onVideoClick(View view, int position) {
                Video video=videoArrayList.get(position);
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+video.key));
                startActivity(intent);
            }
        });
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
            watchlistButton.setImageResource(R.mipmap.watchlist);
            watchlistButtonClicked=false;
        }
        else if(name!=null)
        {
            watchlistButton.setImageResource(R.mipmap.watchlist_fill);
            watchlistButtonClicked=true;
        }

        collapsingToolbarLayout.setTitle(tvShowName);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        titleTextView.setVisibility(View.GONE);
        backdropImageView.setVisibility(View.GONE);
        overviewTextView.setVisibility(View.GONE);
        starImageView.setVisibility(View.GONE);
        ratingTextView.setVisibility(View.GONE);
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
        titleTextView.setText(tvShowName);

        backdropImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=view.getId();
                if(id==R.id.backdropImageView)
                {
                    Intent intent=new Intent(TvShowDetailsActivity.this,ImageActivity.class);
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
        firstAirDateTextView.setText("First Air Date: "+firstAirDate);

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

        Call<CastResponse> call1=ApiClient.getTvService().getCast(tvShowId);
        call1.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call1, Response<CastResponse> response) {
                CastResponse castResponse=response.body();
                ArrayList<Cast> castList=castResponse.cast;
                actorsList.clear();
                actorsList.addAll(castList);
                castRecyclerView.setVisibility(View.VISIBLE);
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
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

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
                starImageView.setVisibility(View.VISIBLE);
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
        final Snackbar removeSnackBar=Snackbar.make(view,"Removed from Watchlist",Snackbar.LENGTH_LONG).setActionTextColor(Color.parseColor("#FF7F50"));
        final Snackbar addSnackBar=Snackbar.make(view,"Added to Watchlist",Snackbar.LENGTH_LONG).setActionTextColor(Color.parseColor("#FF7F50"));
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
        watchlistButton.setImageResource(R.mipmap.watchlist_fill);
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
        watchlistButton.setImageResource(R.mipmap.watchlist);
        watchlistButtonClicked=false;
        WatchlistOpenHelper openHelper=WatchlistOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database=openHelper.getWritableDatabase();
        String[] selectionArgs={tvShowId+""};
        database.delete(ContractTv.Tv.TABLE_NAME,ContractTv.Tv.COLUMN_ID+" =?",selectionArgs);
    }
}
