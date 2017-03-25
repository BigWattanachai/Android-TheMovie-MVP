package com.wattanachai.com.themovie.presenter;

import android.content.Context;

import com.wattanachai.com.themovie.network.ApiClient;
import com.wattanachai.com.themovie.network.model.Movie;
import com.wattanachai.com.themovie.network.model.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BiG on 3/25/2017 AD.
 */

public class MoviePresenter {
    private final Context context;
    private final MoviePresenterListener mListener;
    private final ApiClient apiClient;

    public interface MoviePresenterListener {
        void moviesReady(List<Movie> countries);
    }


    public MoviePresenter(MoviePresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient();
    }

    public void getMovies(String apiKey) {
        apiClient.getMovieClient().getTopRatedMovies(apiKey)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse>
                            response) {
                        MovieResponse result = response.body();
                        if (null != result) {
                            mListener.moviesReady(result.getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        try {
                            throw new InterruptedException("Error occur while get api!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
