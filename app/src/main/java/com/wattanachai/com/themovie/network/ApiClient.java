package com.wattanachai.com.themovie.network;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by BiG on 3/25/2017 AD.
 */

public class ApiClient {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public MovieService getMovieClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(MovieService.class);
    }
}
