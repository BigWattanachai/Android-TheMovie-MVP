package com.wattanachai.com.themovie.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wattanachai.com.themovie.R;
import com.wattanachai.com.themovie.network.model.Movie;
import com.wattanachai.com.themovie.presenter.MoviePresenter;
import com.wattanachai.com.themovie.ui.adapter.MoviesAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviePresenter
        .MoviePresenterListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    MoviePresenter moviePresenter;
    private final static String API_KEY = "32c7ee8f67b752a2845f130de5bff1d3";
    @BindView(R.id.movie_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        moviePresenter = new MoviePresenter(this, this);
        moviePresenter.getMovies(API_KEY);
    }

    @Override
    public void moviesReady(List<Movie> movies) {
        recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie,
                getApplicationContext()));
        Log.d(TAG, "Number of movies received: " + movies.size());
    }
}
