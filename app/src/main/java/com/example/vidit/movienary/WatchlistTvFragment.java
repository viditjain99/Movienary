package com.example.vidit.movienary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hereshem.lib.recycler.MyRecyclerView;

import java.util.ArrayList;

public class WatchlistTvFragment extends Fragment
{
    MyRecyclerView watchlistTvShowsRecyclerView;
    WatchlistTvFragmentCallback listener;
    static TvAdapter adapter;
    static ArrayList<Tv> watchlistTvShows=new ArrayList<>();
    LottieAnimationView emptyList;
    TextView emptyListTextView;

    public WatchlistTvFragment()
    {

    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof WatchlistTvFragmentCallback)
        {
            listener=(WatchlistTvFragmentCallback) context;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View output=inflater.inflate(R.layout.fragment_tv_watchlist,container,false);
        watchlistTvShowsRecyclerView=output.findViewById(R.id.watchlistTvShowsRecyclerView);
        watchlistTvShows.clear();
        emptyList=output.findViewById(R.id.emptyList);
        emptyListTextView=output.findViewById(R.id.emptyListTextView);
        adapter=new TvAdapter(getContext(), watchlistTvShows, new TvClickListener() {
            @Override
            public void onTvClick(View view, int position) {
                Tv tvShow = watchlistTvShows.get(position);
                if (listener != null) {
                    listener.onTvShowSelected(tvShow);
                }
            }
        });
        watchlistTvShowsRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        watchlistTvShowsRecyclerView.setLayoutManager(layoutManager);
        WatchlistOpenHelper openHelper=WatchlistOpenHelper.getInstance(getContext().getApplicationContext());
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(ContractTv.Tv.TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            String tvShowName=cursor.getString(cursor.getColumnIndex(ContractTv.Tv.COLUMN_TVSHOWNAME));
            String overview=cursor.getString(cursor.getColumnIndex(ContractTv.Tv.COLUMN_OVERVIEW));
            String backdropPath=cursor.getString(cursor.getColumnIndex(ContractTv.Tv.COLUMN_BACKDROPPATH));
            String posterPath=cursor.getString(cursor.getColumnIndex(ContractTv.Tv.COLUMN_POSTERPATH));
            String rating=cursor.getString(cursor.getColumnIndex(ContractTv.Tv.COLUMN_RATING));
            String mediaType=cursor.getString(cursor.getColumnIndex(ContractTv.Tv.COLUMN_MEDIATYPE));
            String id=cursor.getString(cursor.getColumnIndex(ContractTv.Tv.COLUMN_ID));
            String firstAirDate=cursor.getString(cursor.getColumnIndex(ContractTv.Tv.COLUMN_FIRSTAIRDATE));
            Tv tvShow=new Tv();
            tvShow.id=id;
            tvShow.tvShowName=tvShowName;
            tvShow.mediaType=mediaType;
            tvShow.backdropPath=backdropPath;
            tvShow.posterPath=posterPath;
            tvShow.overview=overview;
            tvShow.rating=rating;
            tvShow.firstAirDate=firstAirDate;
            watchlistTvShows.add(tvShow);
        }
        if(watchlistTvShows.size()==0)
        {
            emptyList.setVisibility(View.VISIBLE);
            emptyListTextView.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
        return output;
    }

    public interface WatchlistTvFragmentCallback
    {
        void onTvShowSelected(Tv tvShow);
    }
}
