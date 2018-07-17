package com.example.vidit.movienary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder>
{
    ArrayList<Review> reviews;
    Context context;
    ReviewAdapter(Context context,ArrayList<Review> reviews)
    {
        this.context=context;
        this.reviews=reviews;
    }
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowLayout=inflater.inflate(R.layout.reviews_layout,viewGroup,false);
        return new ReviewViewHolder(rowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        Review review=reviews.get(i);
        reviewViewHolder.author.setText(review.author);
        reviewViewHolder.body.setText(review.content);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
