package com.example.vidit.movienary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchViewHolder extends RecyclerView.ViewHolder
{
    ImageView image,star;
    TextView rating,type;
    SearchResultsTextView overview;
    SearchResultsNameExTextView name;
    public SearchViewHolder(@NonNull View itemView)
    {
        super(itemView);
        image=itemView.findViewById(R.id.searchResultsPosterImageView);
        star=itemView.findViewById(R.id.starImageView);
        name=itemView.findViewById(R.id.searchResultsNameTextView);
        rating=itemView.findViewById(R.id.searchResultsRatingTextView);
        type=itemView.findViewById(R.id.searchResultsTypeTextView);
        overview=itemView.findViewById(R.id.searchResultsOverviewTextView);
    }
}
