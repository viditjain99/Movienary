package com.example.vidit.movienary;

import android.content.Intent;
import android.graphics.Color;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity implements PopularMoviesFragment.PopularMoviesFragmentCallBack,TopRatedMoviesFragment.TopRatedMoviesFragmentCallBack,NowShowingMoviesFragment.NowShowingMoviesFragmentCallBack,UpcomingMoviesFragment.UpcomingMoviesFragmentCallBack,NavigationView.OnNavigationItemSelectedListener
{
    PopularMoviesFragment popularMoviesFragment;
    TopRatedMoviesFragment topRatedMoviesFragment;
    NowShowingMoviesFragment nowShowingMoviesFragment;
    UpcomingMoviesFragment upcomingMoviesFragment;
    LottieAnimationView loading;
    Toolbar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            if(popularMoviesFragment==null)
            {
                popularMoviesFragment=new PopularMoviesFragment();
            }
            if(topRatedMoviesFragment==null)
            {
                topRatedMoviesFragment=new TopRatedMoviesFragment();
            }
            if(nowShowingMoviesFragment==null)
            {
                nowShowingMoviesFragment=new NowShowingMoviesFragment();
            }
            if(upcomingMoviesFragment==null)
            {
                upcomingMoviesFragment=new UpcomingMoviesFragment();
            }
            switch (item.getItemId()) {
                case R.id.navigation_popular:
                    transaction.replace(R.id.contentContainer,popularMoviesFragment);
                    break;
                case R.id.navigation_top_rated:
                    transaction.replace(R.id.contentContainer,topRatedMoviesFragment);
                    break;
                case R.id.navigation_now_showing:
                    transaction.replace(R.id.contentContainer,nowShowingMoviesFragment);
                    break;
                case R.id.navigation_upcoming:
                    transaction.replace(R.id.contentContainer,upcomingMoviesFragment);
                    break;
            }
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movies");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loading=findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        toolbar.setTitle("Movies");
        transaction.commit();
        popularMoviesFragment=new PopularMoviesFragment();
        transaction.replace(R.id.contentContainer,popularMoviesFragment);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(popularMoviesFragment==null)
        {
            popularMoviesFragment=new PopularMoviesFragment();
        }
        if(topRatedMoviesFragment==null)
        {
            topRatedMoviesFragment=new TopRatedMoviesFragment();
        }
        if(nowShowingMoviesFragment==null)
        {
            nowShowingMoviesFragment=new NowShowingMoviesFragment();
        }
        if(upcomingMoviesFragment==null)
        {
            upcomingMoviesFragment=new UpcomingMoviesFragment();
        }
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        int id = menuItem.getItemId();

        if (id == R.id.nav_movies)
        {
            toolbar.setTitle("Movies");
            transaction.replace(R.id.contentContainer,popularMoviesFragment);
        }
        else if (id == R.id.nav_tv_shows)
        {
            toolbar.setTitle("TV Shows");
        }
        else if (id == R.id.nav_watchlist)
        {
            toolbar.setTitle("Watchlist");
        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
