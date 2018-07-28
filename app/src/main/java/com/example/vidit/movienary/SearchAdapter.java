package com.example.vidit.movienary;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>
{
    ArrayList<Movie> searchResults;
    Context context;
    SearchResultClickListener listener;
    SearchAdapter(ArrayList<Movie> searchResults,Context context,SearchResultClickListener listener)
    {
        this.searchResults=searchResults;
        this.context=context;
        this.listener=listener;

    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View resultLayout=inflater.inflate(R.layout.search_result_layout,viewGroup,false);
        return new SearchViewHolder(resultLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder searchViewHolder, int i)
    {
        Movie result=searchResults.get(i);
        searchViewHolder.overview.setVisibility(View.VISIBLE);
        searchViewHolder.star.setVisibility(View.VISIBLE);
        searchViewHolder.rating.setVisibility(View.VISIBLE);
        String mediaType=result.mediaType;
        if(mediaType.equals("movie"))
        {
            mediaType="Movie";
            searchViewHolder.name.setText(result.movieName);
        }
        if(mediaType.equals("tv"))
        {
            mediaType="TV Show";
            searchViewHolder.name.setText(result.tvShowName);
        }
        if(mediaType.equals("person"))
        {
            mediaType="Person";
            searchViewHolder.name.setText(result.tvShowName);
            searchViewHolder.type.setText(mediaType);
            searchViewHolder.image.setBackgroundResource(R.drawable.user);
            searchViewHolder.overview.setVisibility(View.GONE);
            searchViewHolder.star.setVisibility(View.GONE);
            searchViewHolder.rating.setVisibility(View.GONE);
            return;
        }
        searchViewHolder.type.setText(mediaType);
        searchViewHolder.overview.setText(result.overview);
        Picasso.get().load("http://image.tmdb.org/t/p/w185"+result.posterPath).into(searchViewHolder.image);
        if(Float.parseFloat(result.rating)==0)
        {
            searchViewHolder.rating.setText("N/A");
        }
        else
        {
            searchViewHolder.rating.setText(result.rating);
        }
        Picasso.get().load("https://cdn2.iconfinder.com/data/icons/modifiers-add-on-1-flat/48/Mod_Add-On_1-35-512.png").into(searchViewHolder.star);
        searchViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onResultClick(view,searchViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return searchResults.size();
    }
}
