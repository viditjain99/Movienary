package com.example.vidit.movienary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    ArrayList<Movie> movies;
    Context context;
    MovieClickListener listener;
    MovieAdapter(Context context,ArrayList<Movie> movies,MovieClickListener movieClickListener)
    {
        this.context=context;
        this.movies=movies;
        this.listener=movieClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowLayout=inflater.inflate(R.layout.row_layout,viewGroup,false);
        return new MovieViewHolder(rowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder movieViewHolder, int i)
    {
        Movie movie=movies.get(i);
        movieViewHolder.name.setText(movie.movieName);
        Picasso.get().load("http://image.tmdb.org/t/p/w500/"+movie.posterPath).into(movieViewHolder.image);
        //Glide.with(context.getApplicationContext()).load("http://image.tmdb.org/t/p/w500/"+movie.posterPath).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(movieViewHolder.image);
        if(Float.parseFloat(movie.rating)==0)
        {
            movieViewHolder.rating.setText("N/A");
        }
        else
        {
            movieViewHolder.rating.setText(movie.rating);
        }
        Picasso.get().load("https://cdn2.iconfinder.com/data/icons/modifiers-add-on-1-flat/48/Mod_Add-On_1-35-512.png").into(movieViewHolder.star);
        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMovieClick(view,movieViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
