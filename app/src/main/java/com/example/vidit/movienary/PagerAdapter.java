package com.example.vidit.movienary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter
{
    int numOfTabs;

    public PagerAdapter(FragmentManager fm,int numOfTabs)
    {
        super(fm);
        this.numOfTabs=numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                WatchlistMovieFragment watchlistMovieFragment = new WatchlistMovieFragment();
                return watchlistMovieFragment;
            case 1:
                WatchlistTvFragment watchlistTvFragment=new WatchlistTvFragment();
                return watchlistTvFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
