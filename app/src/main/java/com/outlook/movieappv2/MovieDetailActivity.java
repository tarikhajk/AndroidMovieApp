package com.outlook.movieappv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.outlook.movieappv2.Adapters.MovieSearchAdapter;
import com.outlook.movieappv2.Model.MovieResponseResults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MovieDetailActivity extends AppCompatActivity {

    private CircleImageView movieDetailPosterImageView;
    private ImageView movieDetailBackdropCircleImageView;
    private AppCompatTextView movieDetailTitle;
    private ArcProgress movieDetailRatingArcProgress;
    private MaterialFavoriteButton favoriteButton;

    private MovieResponseResults currentMovie;
    private List<MovieResponseResults> favoritesList;

    //private LinearLayoutCompat movieDetailReleaseDateLayout;
    //private LinearLayoutCompat movieDetailOverviewLayout;

    private AppCompatTextView movieDetailReleaseDate;
    private AppCompatTextView movieDetailOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        movieDetailPosterImageView = findViewById(R.id.movieDetailPosterImageView);
        movieDetailBackdropCircleImageView = findViewById(R.id.movieDetailBackdropCircleImageView);
        movieDetailTitle = findViewById(R.id.movieDetailTitle);
        movieDetailRatingArcProgress = findViewById(R.id.movieDetailRatingArcProgress);
        favoriteButton = findViewById(R.id.favoriteButton);

        //movieDetailReleaseDateLayout = findViewById(R.id.movieDetailReleaseDateLayout);
        //movieDetailOverviewLayout = findViewById(R.id.movieDetailOverviewLayout);

        movieDetailReleaseDate = findViewById(R.id.movieDetailReleaseDateTextView);
        movieDetailOverview = findViewById(R.id.movieDetailOverviewTextView);

        if (intent != null && intent.getExtras() != null) {

            currentMovie = intent.getExtras().getParcelable("current_movie");
            favoritesList = intent.getExtras().getParcelableArrayList("favorites_list");

            // get favorite button state
            if (favoritesList.contains(currentMovie)) {
                favoriteButton.setFavorite(true);
            }

            String posterPath = intent.getExtras().getString("poster_path","null");
            String title = intent.getExtras().getString("title","null");
            String backdropPath = intent.getExtras().getString("backdrop_path","null");
            String voteAverage = intent.getExtras().getString("vote_average", "null");
            String releaseDate = intent.getExtras().getString("release_date", "null");
            String overview = intent.getExtras().getString("overview", "null");

            Picasso.with(this).load(posterPath).into(movieDetailPosterImageView);
            Picasso.with(this).load(backdropPath).into(movieDetailBackdropCircleImageView);

            movieDetailRatingArcProgress.setProgress((int) (Double.parseDouble(voteAverage)*10));

            movieDetailTitle.setText(title);
            // this allows text scrolling when poster title is too long
            movieDetailTitle.setSelected(true);

            if (releaseDate != null && releaseDate.length() > 0) {
                movieDetailReleaseDate.setText(releaseDate);
                movieDetailReleaseDate.setVisibility(View.VISIBLE);
            } else {
                movieDetailReleaseDate.setVisibility(View.GONE);
            }

            if (overview != null && overview.length() > 0) {
                movieDetailOverview.setText(overview);
                movieDetailOverview.setVisibility(View.VISIBLE);
            } else {
                movieDetailOverview.setVisibility(View.GONE);
            }
        }

        // favorites button is not retaining true/false state??
        favoriteButton.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (!favoritesList.contains(currentMovie)) {
                            favoritesList.add(currentMovie);
                            favoriteButton.setFavorite(true);
                        } else {
                            favoritesList.remove(currentMovie);
                            favoriteButton.setFavorite(false);
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putParcelableArrayListExtra("favorites_list", (ArrayList<? extends Parcelable>) favoritesList);
        startActivity(intent);

        // i couldnt get startActivityOnResult() working
        //setResult(Activity.RESULT_OK, intent);
        //finish();
    }
}
