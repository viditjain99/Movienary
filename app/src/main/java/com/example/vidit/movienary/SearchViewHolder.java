package com.example.vidit.movienary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchViewHolder extends RecyclerView.ViewHolder
{
    ImageView image;
    TextView text;
    public SearchViewHolder(@NonNull View itemView)
    {
        super(itemView);
        image=itemView.findViewById(R.id.searchResultImageView);
        text=itemView.findViewById(R.id.searchResultTextView);
    }
}
