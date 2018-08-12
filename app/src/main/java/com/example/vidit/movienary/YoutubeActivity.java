package com.example.vidit.movienary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener
{
    String videoKey="";
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        getWindow().setStatusBarColor(Color.BLACK);

        Intent intent=getIntent();
        videoKey=intent.getStringExtra("Key");
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        youTubeView.initialize("AIzaSyBtrXiPcNbEMns0DQxAbIwC0MYYJ96X_FY", this);
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(videoKey);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
    {
        Toast.makeText(this,"An Unknown Error Occurred",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
