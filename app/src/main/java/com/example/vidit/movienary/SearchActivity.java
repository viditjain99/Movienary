package com.example.vidit.movienary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    EditText searchEditText;
    TextView resultsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchEditText = findViewById(R.id.searchEditText);
        resultsTextView = findViewById(R.id.resultsTextView);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    public void search() {
        String query = searchEditText.getText().toString();
        Call<MovieResponse> call = ApiClient.getMoviesService().getSearchResults(query, 1);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                ArrayList<Movie> moviesList = movieResponse.results;
                for (int i = 0; i < moviesList.size(); i++) {
                    if (moviesList.get(i).mediaType.equals("movie"))
                    {
                        resultsTextView.setText(resultsTextView.getText() + " " + moviesList.get(i).movieName);
                    }
                    else
                    {
                        resultsTextView.setText(resultsTextView.getText() + " " + moviesList.get(i).actorName);
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
}
