package com.example.vidit.movienary;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class TrendingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        TrendingMoviesFragment.TrendingMoviesFragmentCallBack,TrendingTvShowsFragment.TrendingTvShowsFragmentCallBack,
        TrendingPeopleFragment.TrendingPeopleFragmentCallBack
{
    TrendingMoviesFragment trendingMoviesFragment;
    TrendingTvShowsFragment trendingTvShowsFragment;
    TrendingPeopleFragment trendingPeopleFragment;
    LottieAnimationView loading;
    Toolbar toolbar;
    MenuItem searchItem;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
            //transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
            if(trendingMoviesFragment==null)
            {
                trendingMoviesFragment=new TrendingMoviesFragment();
                //transaction.setCustomAnimations(R.anim.exit_to_left,R.anim.enter_from_right);
            }
            if(trendingTvShowsFragment==null)
            {
                trendingTvShowsFragment=new TrendingTvShowsFragment();
                //transaction.setCustomAnimations(R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left);
            }
            if(trendingPeopleFragment==null)
            {
                //transaction.setCustomAnimations(R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left);
                trendingPeopleFragment=new TrendingPeopleFragment();
            }
            switch (item.getItemId()) {
                case R.id.navigation_trending_movie:
                    transaction.replace(R.id.contentContainer,trendingMoviesFragment);
                    break;
                case R.id.navigation_trending_tv:
                    transaction.replace(R.id.contentContainer,trendingTvShowsFragment);
                    break;
                case R.id.navigation_trending_people:
                    transaction.replace(R.id.contentContainer,trendingPeopleFragment);
                    break;
            }
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.BLACK);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Trending");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_trending);
        loading=findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        trendingMoviesFragment=new TrendingMoviesFragment();
        transaction.replace(R.id.contentContainer,trendingMoviesFragment);
        transaction.commit();
        loading.setVisibility(View.GONE);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

    @Override
    public void onTvShowSelected(Tv tvShow)
    {
        Bundle bundle=new Bundle();
        bundle.putString("TvShowName",tvShow.tvShowName);
        bundle.putString("PosterPath",tvShow.posterPath);
        bundle.putString("Rating",tvShow.rating);
        bundle.putString("Id",tvShow.id);
        bundle.putString("BackdropPath",tvShow.backdropPath);
        bundle.putString("Overview",tvShow.overview);
        bundle.putString("FirstAirDate",tvShow.firstAirDate);
        Intent intent=new Intent(this,TvShowDetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onActorSelected(Actor actor)
    {
        String id=actor.id;
        Intent intent1=new Intent(TrendingActivity.this,ActorDetailsActivity.class);
        intent1.putExtra("Id",id);
        startActivity(intent1);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            if(mBackPressed+TIME_INTERVAL>System.currentTimeMillis())
            {
                super.onBackPressed();
                return;
            }
            else
            {
                Toast.makeText(this,"Press again to exit",Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        searchItem=menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search for Movies and TV Shows");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent=new Intent(TrendingActivity.this,SearchActivity.class);
                intent.putExtra("Query",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query=newText;
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(trendingMoviesFragment==null)
        {
            trendingMoviesFragment=new TrendingMoviesFragment();
        }
        if(trendingPeopleFragment==null)
        {
            trendingPeopleFragment=new TrendingPeopleFragment();
        }
        if(trendingTvShowsFragment==null)
        {
            trendingTvShowsFragment=new TrendingTvShowsFragment();
        }
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right);
        int id = menuItem.getItemId();

        if (id == R.id.nav_movies)
        {
            toolbar.setTitle("Movies");
            Intent intent=new Intent(TrendingActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_tv_shows)
        {
            toolbar.setTitle("TV Shows");
            Intent intent=new Intent(TrendingActivity.this,TvShowsActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_watchlist)
        {
            toolbar.setTitle("Watchlist");
            Intent intent=new Intent(TrendingActivity.this,WatchListActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.nav_trending)
        {

        }
        else if (id == R.id.nav_share)
        {
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,"Hey check out the ShowBuzz app at:https://play.google.com/store/apps/details?id=com.vidit.vidit.movienary");
            intent.setType("text/plain");
            startActivity(intent);
        }
        else if (id == R.id.nav_send)
        {
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            Uri uri=Uri.parse("mailto:vidit1000@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback of Movienary App");
            intent.setData(uri);
            startActivity(intent);
        }
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
