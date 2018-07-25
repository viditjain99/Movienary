package com.example.vidit.movienary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    ArrayList<Video> videoArrayList;
    Context context;
    VideoClickListener listener;
    VideoAdapter(Context context,ArrayList<Video> videoArrayList,VideoClickListener listener)
    {
        this.context=context;
        this.videoArrayList=videoArrayList;
        this.listener=listener;
    }
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View videoRowLayout=inflater.inflate(R.layout.video_row_layout,viewGroup,false);
        return new VideoViewHolder(videoRowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoViewHolder videoViewHolder, int i)
    {
        Video video=videoArrayList.get(i);
//        String title=video.type;
//        String t="";
//        if(title!=null)
//        {
//            char ch=title.charAt(0);
//            int ascii=(int) ch;
//            int nAscii=ch-32;
//            char capCh=(char) nAscii;
//            t=t+capCh;
//            for(int j=1;j<title.length();j++)
//            {
//                t=t+title.charAt(j);
//            }
//        }
        if(video.name==null)
        {
            videoViewHolder.title.setText(video.type);
        }
        else
        {
            videoViewHolder.title.setText(video.name);
        }
        Picasso.get().load("https://img.youtube.com/vi/"+video.key+"/hqdefault.jpg").into(videoViewHolder.thumbnail);
        videoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onVideoClick(view,videoViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }
}
