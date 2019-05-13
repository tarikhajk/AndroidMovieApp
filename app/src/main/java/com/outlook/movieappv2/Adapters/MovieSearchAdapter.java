package com.outlook.movieappv2.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outlook.movieappv2.Model.MovieResponseResults;
import com.outlook.movieappv2.MovieDetailActivity;
import com.outlook.movieappv2.R;
import com.outlook.movieappv2.ViewHolders.MovieSearchViewHolder;

import java.util.ArrayList;
import java.util.List;


public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchViewHolder> {

    private Activity activity;
    private List<MovieResponseResults> movieList;
    private List<MovieResponseResults> favoritesList;
    private MovieResponseResults currentMovie;
    private int layout;

    public MovieSearchAdapter(Activity activity, int layout, List<MovieResponseResults> movieList,
                              List<MovieResponseResults> favoritesList) {
        this.layout = layout;
        this.activity = activity;
        this.movieList = movieList;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public MovieSearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(layout,viewGroup,false);
        return new MovieSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieSearchViewHolder movieSearchViewHolder, int i) {
        if (layout == R.layout.search_layout_items) {
            currentMovie = movieList.get(i);
        } else if (layout == R.layout.favorites_layout_items) {
            currentMovie = favoritesList.get(i);
        }
        movieSearchViewHolder.setPosterImageView(activity, currentMovie.getPoster_path());

        String title = currentMovie.getTitle();
        if (title != null) {
            movieSearchViewHolder.posterTextView.setVisibility(View.VISIBLE);
            movieSearchViewHolder.posterTextView.setText(title);
            // this allows text scrolling when poster title is too long
            movieSearchViewHolder.posterTextView.setSelected(true);
        } else {
            movieSearchViewHolder.posterTextView.setVisibility(View.GONE);
        }

        movieSearchViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MovieDetailActivity.class);
                if (layout == R.layout.search_layout_items) {
                    currentMovie = movieList.get(movieSearchViewHolder.getAdapterPosition());
                } else if (layout == R.layout.favorites_layout_items) {
                    currentMovie = favoritesList.get(movieSearchViewHolder.getAdapterPosition());
                }

                intent.putExtra("current_movie", currentMovie);
                intent.putParcelableArrayListExtra("favorites_list", (ArrayList<? extends Parcelable>) favoritesList);

                intent.putExtra("poster_path", String.valueOf(currentMovie.getPoster_path()));
                intent.putExtra("title", String.valueOf(currentMovie.getTitle()));
                intent.putExtra("backdrop_path", String.valueOf(currentMovie.getBackdrop_path()));
                intent.putExtra("vote_average", String.valueOf(currentMovie.getVote_average()));
                intent.putExtra("release_date", String.valueOf(currentMovie.getRelease_date()));
                intent.putExtra("overview", String.valueOf(currentMovie.getOverview()));
                activity.startActivity(intent);

                //activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (layout == R.layout.search_layout_items) {
            return movieList.size();
        } else if (layout == R.layout.favorites_layout_items){
            return favoritesList.size();
        }
        // need to define new layout if using this adapter again
        return -1;
    }
}
