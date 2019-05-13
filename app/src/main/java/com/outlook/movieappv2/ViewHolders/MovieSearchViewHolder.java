package com.outlook.movieappv2.ViewHolders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.outlook.movieappv2.R;
import com.squareup.picasso.Picasso;

public class MovieSearchViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView posterImageView;
    public AppCompatTextView posterTextView;

    public MovieSearchViewHolder(@NonNull View itemView) {
        super(itemView);

        posterImageView = itemView.findViewById(R.id.posterImageView);
        posterTextView = itemView.findViewById(R.id.posterTextView);
    }

    public void setPosterImageView(Context context, String posterUrl) {
        Picasso.with(context).load(posterUrl).into(posterImageView);
    }

    public void setPosterTextView(AppCompatTextView posterTextView) {
        this.posterTextView = posterTextView;
    }
}
