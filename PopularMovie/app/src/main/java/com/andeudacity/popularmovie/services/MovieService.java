package com.andeudacity.popularmovie.services;

import android.app.Application;

import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.entities.Review;
import com.andeudacity.popularmovie.entities.Video;
import com.andeudacity.popularmovie.services.dto.MovieListResponseDto;
import com.andeudacity.popularmovie.services.dto.ReviewListResponseDto;
import com.andeudacity.popularmovie.services.dto.VideoListResponseDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created by andrii on 4/30/18.
 */

public class MovieService implements IMovieService {

    private final String mApiUri = "https://api.themoviedb.org/3";

    private final String apiKey;

    private final OkHttpClient mClient;

//    private final String mYoutubeBase = "https://www.youtube.com/watch?v=";

    public MovieService(String apiKey){
        this.apiKey = apiKey;
        mClient = new OkHttpClient();
    }

    @Override
    public ServiceResult<List<Movie>> loadPopularMovie(int page) {
        String requestUrl = "/movie/popular?page="+page;
        return loadMovies(requestUrl);
    }

    @Override
    public ServiceResult<List<Movie>> loadTopRateMovie(int page) {
        String requestUrl = "/movie/top_rated?page="+page;
        return loadMovies(requestUrl);
    }

    @Override
    public ServiceResult<List<Video>> loadVideo(long id) {
        String requestUrl = "/movie/" + id + "/videos?";
        Request request = getRequestBuilder(requestUrl)
                .get()
                .build();

        Response response = null;

        try {
            response = mClient.newCall(request).execute();

            if (response.isSuccessful()) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                String responseJson = response.body().string();

                VideoListResponseDto responseDto = gson.fromJson(responseJson, VideoListResponseDto.class);

                if (responseDto == null) {
                    responseDto = new VideoListResponseDto();
                }

                return ServiceResult.success(responseDto.getResults());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //todo: log errors
        }

        return handleFailedResponse(response);
    }

    @Override
    public ServiceResult<List<Review>> loadReview(long id) {
        String requestUrl = "/movie/" + id + "/reviews?";
        Request request = getRequestBuilder(requestUrl)
                .get()
                .build();

        Response response = null;

        try {
            response = mClient.newCall(request).execute();

            if (response.isSuccessful()) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                String responseJson = response.body().string();

                ReviewListResponseDto responseDto = gson.fromJson(responseJson, ReviewListResponseDto.class);

                if (responseDto == null) {
                    responseDto = new ReviewListResponseDto();
                }

                return ServiceResult.success(responseDto.getResults());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //todo: log errors
        }

        return handleFailedResponse(response);
    }

    private ServiceResult<List<Movie>> loadMovies(String requestUrl){
        Request request = getRequestBuilder(requestUrl)
                .get()
                .build();

        Response response = null;

        try {
            response = mClient.newCall(request).execute();

            if (response.isSuccessful()) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                String responseJson = response.body().string();

                MovieListResponseDto responseDto = gson.fromJson(responseJson, MovieListResponseDto.class);

                if (responseDto == null) {
                    responseDto = new MovieListResponseDto();
                }

                return ServiceResult.success(responseDto.getResults());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //todo: log errors
        }

        return handleFailedResponse(response);
    }



    private Request.Builder getRequestBuilder(String url){
        //https://api.themoviedb.org/3/movie/popular?api_key=ceebca0c4656ce0ac814b644726dba37&page=1

        return new Request.Builder()
                .url(mApiUri + url + "&" + "api_key=" + apiKey );
    }



    private <E> ServiceResult<E> handleFailedResponse(Response response) {
        return ServiceResult.error("Can't connect to server!", null); //how would I use value.strings in this case?
    }

}
