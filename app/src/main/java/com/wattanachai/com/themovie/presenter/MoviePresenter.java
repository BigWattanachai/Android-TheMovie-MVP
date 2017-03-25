package com.wattanachai.com.themovie.presenter;

import com.wattanachai.com.themovie.network.ApiClient;
import com.wattanachai.com.themovie.network.model.MovieResponse;
import com.wattanachai.com.themovie.ui.main.MainView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BiG on 3/25/2017 AD.
 */

public class MoviePresenter {
    private final MainView mainView;
    private final ApiClient apiClient;

    public MoviePresenter(ApiClient apiClient, MainView mainView) {
        this.apiClient = apiClient;
        this.mainView = mainView;
    }

    public void getMovies(String apiKey) {
        apiClient.getMovieClient().getTopRatedMovies(apiKey)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse>
                            response) {
                        MovieResponse result = response.body();
                        if (null != result) {
                            mainView.getMovieListSuccess(result.getResults());
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
