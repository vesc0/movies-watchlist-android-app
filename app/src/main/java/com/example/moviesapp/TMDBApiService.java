package com.example.moviesapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBApiService {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<MovieResponse> searchMovies(@Query("api_key") String apiKey,
                                     @Query("query") String query,
                                     @Query("language") String language,
                                     @Query("page") int page);

}
