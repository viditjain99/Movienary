package com.example.vidit.movienary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TvAdapter extends RecyclerView.Adapter<TvViewHolder>
{
    ArrayList<Tv> tvShows;
    Context context;
    TvClickListener listener;
    TvAdapter(Context context,ArrayList<Tv> tvShows,TvClickListener movieClickListener)
    {
        this.context=context;
        this.tvShows=tvShows;
        this.listener=movieClickListener;
    }
    @NonNull
    @Override
    public TvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowLayout=inflater.inflate(R.layout.row_layout,viewGroup,false);
        return new TvViewHolder(rowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvViewHolder tvViewHolder, int i)
    {
        Tv tvShow=tvShows.get(i);
        tvViewHolder.name.setText(tvShow.tvShowName);
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        int width=metrics.widthPixels;
        if(width>720 && width<=1080)
        {
            Picasso.get().load("http://image.tmdb.org/t/p/w500/"+tvShow.posterPath).into(tvViewHolder.image);
        }
        else if(width>1080 && width<=1440)
        {
            Picasso.get().load("http://image.tmdb.org/t/p/w780/"+tvShow.posterPath).into(tvViewHolder.image);
        }
        else
        {
            Picasso.get().load("http://image.tmdb.org/t/p/w342/"+tvShow.posterPath).into(tvViewHolder.image);
        }
//        float ratio=((float) metrics.heightPixels/(float) metrics.widthPixels);
//        if(ratio<1.8f && ratio>1.33f)
//        {
//            Picasso.get().load("http://image.tmdb.org/t/p/w500/"+tvShow.posterPath).into(tvViewHolder.image);
//        }
//        else
//        {
//            Picasso.get().load("http://image.tmdb.org/t/p/w780/"+tvShow.posterPath).into(tvViewHolder.image);
//        }
        if(Float.parseFloat(tvShow.rating)==0)
        {
            tvViewHolder.rating.setText("N/A");
        }
        else
        {
            tvViewHolder.rating.setText(tvShow.rating);
        }
        Picasso.get().load("https://cdn2.iconfinder.com/data/icons/modifiers-add-on-1-flat/48/Mod_Add-On_1-35-512.png").into(tvViewHolder.star);
        tvViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTvClick(view,tvViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }
}

