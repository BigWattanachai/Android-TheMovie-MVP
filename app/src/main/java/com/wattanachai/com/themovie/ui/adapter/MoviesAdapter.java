package com.wattanachai.com.themovie.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wattanachai.com.themovie.R;
import com.wattanachai.com.themovie.network.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BiG on 3/25/2017 AD.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private int rowLayout;
    private Context context;
    public static final String IMAGE_URL_BASE_PATH = "http://image.tmdb.org/t/p/w342//";

    public MoviesAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movies_layout)
        LinearLayout moviesLayout;
        @BindView(R.id.title)
        TextView movieTitle;
        @BindView(R.id.date)
        TextView data;
        @BindView(R.id.description)
        TextView movieDescription;
        @BindView(R.id.rating)
        TextView rating;
        @BindView(R.id.movie_image)
        ImageView movieImage;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String image_url = IMAGE_URL_BASE_PATH + movies.get(position).getPosterPath();
        Glide.with(context)
                .load(image_url)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.movieImage);
        holder.movieTitle.setText(movies.get(position).getTitle());
        holder.data.setText(movies.get(position).getReleaseDate());
        holder.movieDescription.setText(movies.get(position).getOverview());
        holder.rating.setText(String.valueOf(movies.get(position).getVoteAverage()));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
