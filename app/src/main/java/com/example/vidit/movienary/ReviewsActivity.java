package com.example.vidit.movienary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity
{
    RecyclerView reviewsRecyclerView;
    android.support.v7.widget.Toolbar toolbar;
    ReviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        reviewsRecyclerView=findViewById(R.id.reviewsRecyclerView);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle("Reviews");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Intent intent=getIntent();
        ArrayList<String> authors=intent.getStringArrayListExtra("authors");
        ArrayList<String> content=intent.getStringArrayListExtra("content");
        ArrayList<Review> reviews=new ArrayList<>();
        for(int i=0;i<authors.size();i++)
        {
            String count=String.valueOf(i+1);
            Review review=new Review(count+"."+" "+authors.get(i),content.get(i));
            reviews.add(review);
        }
        adapter=new ReviewAdapter(ReviewsActivity.this,reviews);
        LinearLayoutManager layoutManager=new LinearLayoutManager(ReviewsActivity.this,LinearLayoutManager.VERTICAL,false);
        reviewsRecyclerView.setLayoutManager(layoutManager);
        reviewsRecyclerView.setAdapter(adapter);
    }
}
