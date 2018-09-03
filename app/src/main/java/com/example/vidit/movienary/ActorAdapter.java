package com.example.vidit.movienary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActorAdapter extends RecyclerView.Adapter<ActorViewHolder>
{
    ArrayList<Actor> actorsList;
    Context context;
    ActorClickListener listener;
    ActorAdapter(Context context,ArrayList<Actor> actorsList,ActorClickListener listener)
    {
        this.context=context;
        this.actorsList=actorsList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actorRowLayout=inflater.inflate(R.layout.actor_row_layout,viewGroup,false);
        return new ActorViewHolder(actorRowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final ActorViewHolder actorViewHolder, int i)
    {
        Actor actor=actorsList.get(i);
        String s="<b>"+actor.name+"</b>";
        actorViewHolder.actorName.setText(Html.fromHtml(s));
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        int width=metrics.widthPixels;
        if(width>720 && width<=1080)
        {
            Picasso.get().load("http://image.tmdb.org/t/p/w500/"+actor.profilePath).into(actorViewHolder.actorImage);
        }
        else if(width>1080 && width<=1536)
        {
            Picasso.get().load("http://image.tmdb.org/t/p/w780/"+actor.profilePath).into(actorViewHolder.actorImage);
        }
        else if(width>=1536 && width<=1800)
        {
            Picasso.get().load("http://image.tmdb.org/t/p/original/"+actor.profilePath).into(actorViewHolder.actorImage);
        }
        else
        {
            Picasso.get().load("http://image.tmdb.org/t/p/w342/"+actor.profilePath).into(actorViewHolder.actorImage);
        }
        actorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onActorClick(view,actorViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return actorsList.size();
    }
}
