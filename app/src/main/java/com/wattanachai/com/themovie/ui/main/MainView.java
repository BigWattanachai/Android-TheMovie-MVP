package com.wattanachai.com.themovie.ui.main;

import com.wattanachai.com.themovie.network.model.Movie;

import java.util.List;

/**
 * Created by BiG on 3/26/2017 AD.
 */

public interface MainView {
    void getMovieListSuccess(List<Movie> movies);
}
