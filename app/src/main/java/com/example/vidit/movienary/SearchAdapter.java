package com.example.vidit.movienary;

import android.content.Context;
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
    SearchAdapter(ArrayList<Movie> searchResults,Context context)
    {
        this.searchResults=searchResults;
        this.context=context;
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View resultLayout=inflater.inflate(R.layout.search_result_layout,viewGroup,false);
        return new SearchViewHolder(resultLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i)
    {
        Movie result=searchResults.get(i);
        String mediaType=result.mediaType;
        searchViewHolder.text.setText(result.movieName);
        Picasso.get().load("http://image.tmdb.org/t/p/w500//"+result.posterPath).into(searchViewHolder.image);
    }

    @Override
    public int getItemCount()
    {
        return searchResults.size();
    }
}
