package com.example.vidit.movienary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.api.Api;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PopularMoviesFragment.PopularMoviesFragmentCallBack,
        TopRatedMoviesFragment.TopRatedMoviesFragmentCallBack,NowShowingMoviesFragment.NowShowingMoviesFragmentCallBack,
        UpcomingMoviesFragment.UpcomingMoviesFragmentCallBack,NavigationView.OnNavigationItemSelectedListener
{
    PopularMoviesFragment popularMoviesFragment;
    TopRatedMoviesFragment topRatedMoviesFragment;
    NowShowingMoviesFragment nowShowingMoviesFragment;
    UpcomingMoviesFragment upcomingMoviesFragment;
    String tag="";
    LottieAnimationView loading;
    ArrayList<String> actorIds;
    Toolbar toolbar;
    MenuItem searchItem;
    int count=0;
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
            if(popularMoviesFragment==null)
            {
                popularMoviesFragment=new PopularMoviesFragment();
                //transaction.setCustomAnimations(R.anim.exit_to_left,R.anim.enter_from_right);
            }
            if(topRatedMoviesFragment==null)
            {
                topRatedMoviesFragment=new TopRatedMoviesFragment();
                //transaction.setCustomAnimations(R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left);
            }
            if(nowShowingMoviesFragment==null)
            {
                //transaction.setCustomAnimations(R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left);
                nowShowingMoviesFragment=new NowShowingMoviesFragment();
            }
            if(upcomingMoviesFragment==null)
            {
                //transaction.setCustomAnimations(R.anim.exit_to_right, R.anim.enter_from_left);
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
        getWindow().setStatusBarColor(Color.BLACK);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movies");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        actorIds=new ArrayList<>();
        //searchEditText=findViewById(R.id.searchEditText);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_movies);
        loading=findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        //toolbar.setTitle("Movies");
        popularMoviesFragment=new PopularMoviesFragment();
        transaction.replace(R.id.contentContainer,popularMoviesFragment);
        transaction.commit();
        loading.setVisibility(View.GONE);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        Thread t=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                findBirthdays();
//            }
//        });
//        t.start();
//        Toast.makeText(MainActivity.this,count+"",Toast.LENGTH_LONG).show();
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
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
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
            Intent intent=new Intent(this,TvShowsActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_watchlist)
        {
            Intent intent=new Intent(this,WatchListActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.nav_trending)
        {
            toolbar.setTitle("Trending");
            Intent intent=new Intent(MainActivity.this,TrendingActivity.class);
            startActivity(intent);
            finish();
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
//    public void findBirthdays()
//    {
//        for(int i=1;i<=924;i++)
//        {
//            Call<ActorsResponse> call=ApiClient.getActorsService().getActors(i);
//            call.enqueue(new Callback<ActorsResponse>() {
//                @Override
//                public void onResponse(Call<ActorsResponse> call, Response<ActorsResponse> response)
//                {
//                    ActorsResponse actorsResponse=response.body();
//                    if(actorsResponse.results!=null)
//                    {
//                        ArrayList<Cast> cast = actorsResponse.results;
//                        for (int j = 0; j < cast.size(); j++) {
//                            actorIds.add(cast.get(j).id);
//                        }
//                    }
//                }
//                @Override
//                public void onFailure(Call<ActorsResponse> call, Throwable t) {
//
//                }
//            });
//        }
//        for(int i=0;i<actorIds.size();i++)
//        {
//            Call<Actor> call1= ApiClient.getActorsService().getDetails(actorIds.get(i));
//            call1.enqueue(new Callback<Actor>() {
//                @Override
//                public void onResponse(Call<Actor> call, Response<Actor> response) {
//                    Actor actor=response.body();
//                    String birthday=actor.birthday;
//                    String[] bd=birthday.split("-");
//                    String month=bd[1];
//                    String day=bd[2];
//                    if(day.equals(Calendar.DAY_OF_MONTH) && month.equals(Calendar.MONTH))
//                    {
//                        count++;
//                    }
//                }
//                @Override
//                public void onFailure(Call<Actor> call, Throwable t) {
//
//                }
//            });
//        }
//    }
}
