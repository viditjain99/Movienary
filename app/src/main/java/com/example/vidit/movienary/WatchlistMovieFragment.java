package com.example.vidit.movienary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hereshem.lib.recycler.MyRecyclerView;

import java.util.ArrayList;

public class WatchlistMovieFragment extends Fragment
{
    MyRecyclerView watchlistMoviesRecyclerView;
    WatchlistMovieFragmentCallback listener;
    static MovieAdapter adapter;
    ArrayList<Movie> watchlistMovies=new ArrayList<>();

    public WatchlistMovieFragment()
    {

    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof WatchlistMovieFragmentCallback)
        {
            listener=(WatchlistMovieFragmentCallback) context;
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("See","view");
        View output=inflater.inflate(R.layout.fragment_movie_watchlist,container,false);
        watchlistMoviesRecyclerView=output.findViewById(R.id.watchlistMoviesRecyclerView);
        adapter=new MovieAdapter(getContext(), watchlistMovies, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                Movie movie = watchlistMovies.get(position);
                if (listener != null) {
                    listener.onMovieSelected(movie);
                }
            }
        });
        watchlistMoviesRecyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        watchlistMoviesRecyclerView.setLayoutManager(layoutManager);
        WatchlistOpenHelper openHelper=WatchlistOpenHelper.getInstance(getContext().getApplicationContext());
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(ContractMovies.Movie.TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            String movieName=cursor.getString(cursor.getColumnIndex(ContractMovies.Movie.COLUMN_MOVIENAME));
            String overview=cursor.getString(cursor.getColumnIndex(ContractMovies.Movie.COLUMN_OVERVIEW));
            String backdropPath=cursor.getString(cursor.getColumnIndex(ContractMovies.Movie.COLUMN_BACKDROPPATH));
            String posterPath=cursor.getString(cursor.getColumnIndex(ContractMovies.Movie.COLUMN_POSTERPATH));
            String rating=cursor.getString(cursor.getColumnIndex(ContractMovies.Movie.COLUMN_RATING));
            String mediaType=cursor.getString(cursor.getColumnIndex(ContractMovies.Movie.COLUMN_MEDIATYPE));
            String id=cursor.getString(cursor.getColumnIndex(ContractMovies.Movie.COLUMN_ID));
            String releaseDate=cursor.getString(cursor.getColumnIndex(ContractMovies.Movie.COLUMN_RELEASEDATE));
            Movie movie=new Movie();
            movie.id=id;
            movie.movieName=movieName;
            movie.mediaType=mediaType;
            movie.backdropPath=backdropPath;
            movie.posterPath=posterPath;
            movie.overview=overview;
            movie.rating=rating;
            movie.releaseDate=releaseDate;
            watchlistMovies.add(movie);
        }
        adapter.notifyDataSetChanged();
        return output;
    }

    public interface WatchlistMovieFragmentCallback
    {
        void onMovieSelected(Movie movie);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onStart()
    {
        super.onStart();
        adapter.notifyDataSetChanged();
    }
}
