package com.outlook.movieappv2;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.outlook.movieappv2.Adapters.MovieSearchAdapter;
import com.outlook.movieappv2.Model.MovieResponseResults;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private List<MovieResponseResults> favoritesList;
    private List<MovieResponseResults> movieResponseResults;
    private AppCompatButton homeButton;
    private AppCompatButton favoritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        setupToolbar();

        Intent intent = getIntent();
        if (intent != null) {
            favoritesList = intent.getExtras().getParcelableArrayList("favorites_list");
            movieResponseResults = intent.getExtras().getParcelableArrayList("movie_response_results");
        }

        // get and initialize recyclerview to display favorited movies
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setHasFixedSize(true);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setupRecyclerView(favoritesList);
    }

    private void setupToolbar() {
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHomeIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goHomeIntent);
            }
        });

        favoritesButton = findViewById(R.id.favoritesButton);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContext() != FavoritesActivity.this) {
                    Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                    intent.putParcelableArrayListExtra("favorites_list", (ArrayList<? extends Parcelable>) favoritesList);
                    intent.putParcelableArrayListExtra("movie_response_results", (ArrayList<? extends Parcelable>) movieResponseResults);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupRecyclerView(List<MovieResponseResults> ResponseResults) {
        MovieSearchAdapter movieSearchAdapter = new MovieSearchAdapter(this,
                R.layout.favorites_layout_items, movieResponseResults, favoritesList);
        favoritesRecyclerView.setAdapter(movieSearchAdapter);

        // animation for recyclerview loading
        LayoutAnimationController controller = AnimationUtils.
                loadLayoutAnimation(this, R.anim.layout_slide_right);

        favoritesRecyclerView.setLayoutAnimation(controller);
        favoritesRecyclerView.scheduleLayoutAnimation();
    }

}
