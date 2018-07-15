package com.example.vidit.movienary;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CastViewHolder extends RecyclerView.ViewHolder
{
    TextView name;
    ImageView image;
    public CastViewHolder(View itemView)
    {
        super(itemView);
        image=itemView.findViewById(R.id.castImageView);
        name=itemView.findViewById(R.id.castNameTextView);
    }
}
