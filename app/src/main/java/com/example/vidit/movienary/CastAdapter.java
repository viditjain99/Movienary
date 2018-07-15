package com.example.vidit.movienary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastViewHolder>
{
    ArrayList<Cast> castList;
    Context context;
    CastClickListener listener;
    CastAdapter(Context context,ArrayList<Cast> castList,CastClickListener listener)
    {
        this.context=context;
        this.castList=castList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View castRowLayout=inflater.inflate(R.layout.cast_row_layout,viewGroup,false);
        return new CastViewHolder(castRowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final CastViewHolder castViewHolder, int i)
    {
        Cast cast=castList.get(i);
        String s="<b>"+cast.actorName+"</b>";
        castViewHolder.name.setText(Html.fromHtml(s));
        Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.BLACK).borderWidthDp(0).cornerRadiusDp(0).oval(true).build();
        Picasso.get().load("http://image.tmdb.org/t/p/w185/"+cast.profilePath).transform(transformation).into(castViewHolder.image);
        castViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onActorClick(view,castViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }
}
