package com.example.vidit.movienary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    ImageView image,star;
    TextView name,rating;
    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        image=itemView.findViewById(R.id.posterImageView);
        name=itemView.findViewById(R.id.nameTextView);
        rating=itemView.findViewById(R.id.ratingTextView);
        star=itemView.findViewById(R.id.starImageView);
    }
}
