package com.outlook.movieappv2.Interfaces;

import com.outlook.movieappv2.Model.GenreResponse;
import com.outlook.movieappv2.Model.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    // https://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2014-09-15
    //             &primary_release_date.lte=2014-10-22&page=1&api_key=64b6f3a69e5717b13ed8a56fe4417e71
    @GET("/3/discover/movie")
    Call<MovieResponse> getMoviesOnStart(@Query("primary_release_date.gte") String primary_release_date_gte,
                                         @Query("primary_release_date.lte") String primary_release_date_lte,
                                         @Query("api_key") String api_key);

    // https://api.themoviedb.org/3/search/movie?api_key=64b6f3a69e5717b13ed8a56fe4417e71&query="movie name"
    @GET("/3/search/movie")
    Call<MovieResponse> getMoviesByQuery(@Query("api_key") String api_key, @Query("query") String query);

    // https://api.themoviedb.org/3/genre/movie/list?language=en-US&api_key=64b6f3a69e5717b13ed8a56fe4417e71
    @GET("/3/genre/movie/list")
    Call<GenreResponse> getMovieGenres(@Query("language") String language, @Query("api_key") String api_key);
}
