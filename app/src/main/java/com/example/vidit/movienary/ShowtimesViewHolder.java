package com.example.vidit.movienary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ShowtimesViewHolder extends RecyclerView.ViewHolder
{
    TextView title,time;
    public ShowtimesViewHolder(@NonNull View itemView) {
        super(itemView);
        title=itemView.findViewById(R.id.titleTextView);
        time=itemView.findViewById(R.id.timeTextView);
    }
}
