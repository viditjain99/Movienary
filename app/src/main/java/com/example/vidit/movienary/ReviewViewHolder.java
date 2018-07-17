package com.example.vidit.movienary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ReviewViewHolder extends RecyclerView.ViewHolder
{
    TextView author;
    TextView body;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        author=itemView.findViewById(R.id.authorTextView);
        body= itemView.findViewById(R.id.reviewBodyTextView);
    }
}
