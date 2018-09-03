package com.example.vidit.movienary;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActorViewHolder extends RecyclerView.ViewHolder
{
    TextView actorName;
    ImageView actorImage;
    public ActorViewHolder(View itemView)
    {
        super(itemView);
        actorImage=itemView.findViewById(R.id.actorImageView);
        actorName=itemView.findViewById(R.id.actorNameTextView);
    }
}
