package com.example.vidit.movienary;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoViewHolder extends RecyclerView.ViewHolder
{
    TextView title;
    ImageView thumbnail;
    public VideoViewHolder(View itemView)
    {
        super(itemView);
        title=itemView.findViewById(R.id.titleTextView);
        thumbnail=itemView.findViewById(R.id.thumbnailImageView);
    }
}
