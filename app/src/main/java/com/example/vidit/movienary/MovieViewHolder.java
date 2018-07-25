package com.example.vidit.movienary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    ImageView image,star;
    TextView name,rating;
    LottieAnimationView loading;
    ImageButton watchListButton;
    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        image=itemView.findViewById(R.id.posterImageView);
        name=itemView.findViewById(R.id.nameTextView);
        rating=itemView.findViewById(R.id.ratingTextView);
        star=itemView.findViewById(R.id.starImageView);
        loading=itemView.findViewById(R.id.loading);
        watchListButton=itemView.findViewById(R.id.watchListButton);
    }
}
