package com.example.vidit.movienary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ShowtimesAdapter extends RecyclerView.Adapter<ShowtimesViewHolder> {
    ArrayList<Showtime> showtimes;
    Context context;
    ShowtimesAdapter(Context context,ArrayList<Showtime> showtimes)
    {
        this.context=context;
        this.showtimes=showtimes;
    }

    @NonNull
    @Override
    public ShowtimesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowLayout=inflater.inflate(R.layout.showtimes_layout,viewGroup,false);
        return new ShowtimesViewHolder(rowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimesViewHolder showtimesViewHolder, int i)
    {
        Showtime showtime=showtimes.get(i);
        showtimesViewHolder.title.setText(showtime.movieId);
        showtimesViewHolder.time.setText(showtime.startAt);
    }

    @Override
    public int getItemCount() {
        return showtimes.size();
    }
}
