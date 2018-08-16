package com.example.vidit.movienary;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Currency;
import java.util.List;

public class WatchListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        WatchlistMovieFragment.WatchlistMovieFragmentCallback,WatchlistTvFragment.WatchlistTvFragmentCallback{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Toolbar toolbar;
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.BLACK);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Watchlist");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(WatchListActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_watchlist);

//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout=findViewById(R.id.tabs);
        TabItem moviesTab=findViewById(R.id.moviesTab);
        TabItem tvShowsTab=findViewById(R.id.tvShowsTab);

        final ViewPager viewPager=findViewById(R.id.container);
        final PagerAdapter pagerAdapter=new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        int id = menuItem.getItemId();
        if (id == R.id.nav_movies)
        {
            toolbar.setTitle("Movies");
            Intent intent=new Intent(WatchListActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_tv_shows)
        {
            toolbar.setTitle("TV Shows");
            Intent intent=new Intent(WatchListActivity.this,TvShowsActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.nav_watchlist)
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public void onMovieSelected(Movie movie) {
        Bundle bundle=new Bundle();
        bundle.putString("MovieName",movie.movieName);
        bundle.putString("PosterPath",movie.posterPath);
        bundle.putString("Rating",movie.rating);
        bundle.putString("Id",movie.id);
        bundle.putString("BackdropPath",movie.backdropPath);
        bundle.putString("Overview",movie.overview);
        bundle.putString("ReleaseDate",movie.releaseDate);
        Intent intent=new Intent(WatchListActivity.this,DetailsActivity.class);
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_watch_list, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
