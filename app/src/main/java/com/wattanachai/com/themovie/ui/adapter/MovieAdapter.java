package com.wattanachai.com.themovie.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wattanachai.com.themovie.R;
import com.wattanachai.com.themovie.network.model.Movie;
import com.wattanachai.com.themovie.ui.main.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BiG on 3/26/2017 AD.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int rowLayout;
    private Context context;
    private List<Movie> movies;
    private String errorMsg;
    private boolean retryPageLoad = false;
    private boolean isLoadingAdded = false;
    private static final String IMAGE_URL_BASE_PATH = "http://image.tmdb.org/t/p/w342//";

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private MainView mainView;

    public MovieAdapter(Context context, int rowLayout) {
        this.context = context;
        this.rowLayout = rowLayout;
        this.mainView = (MainView) context;
        movies = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                final MovieViewHolder movieVH = (MovieViewHolder) holder;
                String image_url = IMAGE_URL_BASE_PATH + movie.getPosterPath();
                Glide.with(context)
                        .load(image_url)
                        .error(android.R.drawable.sym_def_app_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(movieVH.mPosterImg);
                movieVH.mMovieTitle.setText(movie.getTitle());


                movieVH.mYear.setText(movie.getReleaseDate().substring(0, 4) + " | " + movie
                        .getOriginalLanguage().toUpperCase());
                movieVH.mMovieDesc.setText(movie.getOverview());
                break;
            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;
                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));
                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View view = inflater.inflate(rowLayout, parent, false);
        viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }


    static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_title)
        TextView mMovieTitle;
        @BindView(R.id.movie_desc)
        TextView mMovieDesc;
        @BindView(R.id.movie_year)
        TextView mYear;
        @BindView(R.id.movie_poster)
        ImageView mPosterImg;
        @BindView(R.id.movie_progress)
        ProgressBar mProgress;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.loadmore_progress)
        ProgressBar mProgressBar;
        @BindView(R.id.loadmore_retry)
        ImageButton mRetryBtn;
        @BindView(R.id.loadmore_errortxt)
        TextView mErrorTxt;
        @BindView(R.id.loadmore_errorlayout)
        LinearLayout mErrorLayout;

        LoadingVH(View view) {
            super(view);
            ButterKnife.bind(this, view);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    mainView.retryPageLoad();
                    break;
            }
        }
    }


    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movies.size() - 1;
        Movie result = getItem(position);

        if (result != null) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Movie getItem(int position) {
        return movies.get(position);
    }

    public void addAll(List<Movie> moveResults) {
        for (Movie result : moveResults) {
            add(result);
        }
    }

    private void add(Movie r) {
        movies.add(r);
        notifyItemInserted(movies.size() - 1);
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movies.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movies.size() - 1);
        if (errorMsg != null) this.errorMsg = errorMsg;
    }
}
