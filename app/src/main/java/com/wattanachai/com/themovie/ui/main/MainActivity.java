package com.wattanachai.com.themovie.ui.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wattanachai.com.themovie.R;
import com.wattanachai.com.themovie.network.ApiClient;
import com.wattanachai.com.themovie.network.model.Movie;
import com.wattanachai.com.themovie.presenter.MoviePresenter;
import com.wattanachai.com.themovie.ui.adapter.MovieAdapter;
import com.wattanachai.com.themovie.utils.PaginationScrollListener;

import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {
    private final static String API_KEY = "32c7ee8f67b752a2845f130de5bff1d3";
    @BindView(R.id.movie_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.main_progress)
    ProgressBar progressBar;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.error_btn_retry)
    Button btnRetry;
    @BindView(R.id.error_txt_cause)
    TextView txtError;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private Boolean isFirstPage;
    MoviePresenter moviePresenter;
    MovieAdapter movieAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initRecyclerView();
        loadFirstPage();
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage();
            }
        });
    }

    private void initRecyclerView() {
        moviePresenter = new MoviePresenter(new ApiClient(), this);
        movieAdapter = new MovieAdapter(this, R.layout.list_item_movie);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener
                (linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                loadMoreMovie();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void loadMoreMovie() {
        isFirstPage = false;
        isLoading = true;
        currentPage += 1;
        moviePresenter.getMovies(API_KEY, currentPage);
    }

    private void loadFirstPage() {
        hideErrorView();
        isFirstPage = true;
        moviePresenter.getMovies(API_KEY, currentPage);
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getMovieListSuccess(List<Movie> movies, int totalPage) {
        if (isFirstPage) {
            progressBar.setVisibility(View.GONE);
            movieAdapter.addAll(movies);
            if (currentPage <= totalPage) movieAdapter.addLoadingFooter();
            else isLastPage = true;
        } else {
            movieAdapter.removeLoadingFooter();
            isLoading = false;
            movieAdapter.addAll(movies);

            if (currentPage != totalPage) movieAdapter.addLoadingFooter();
            else isLastPage = true;
        }
    }

    @Override
    public void getMovieFailed(Throwable t) {
        if (isFirstPage) {
            showErrorView(t);
        } else {
            movieAdapter.showRetry(true, fetchErrorMessage(t));
        }
    }

    @Override
    public void retryPageLoad() {
        loadMoreMovie();
    }

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
