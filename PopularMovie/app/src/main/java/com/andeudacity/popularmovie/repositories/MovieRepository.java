package com.andeudacity.popularmovie.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.andeudacity.popularmovie.AppExecutors;
import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.entities.Review;
import com.andeudacity.popularmovie.entities.Video;
import com.andeudacity.popularmovie.services.IMovieService;
import com.andeudacity.popularmovie.services.ServiceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrii on 4/30/18.
 */

public class MovieRepository implements IMovieRepository {

    private String TAG = "MovieRepository";

    private final AppExecutors executors;
    private final IMovieService movieService;

    public MovieRepository(AppExecutors executors, IMovieService movieService){
        this.executors = executors;
        this.movieService = movieService;
    }

    private final MutableLiveData<ServiceResult<List<Movie>>> popularMoviesLiveData = new MutableLiveData<>();

    private final MutableLiveData<ServiceResult<List<Movie>>> topRatedMoviesLIveData = new MutableLiveData<>();

    @Override
    public LiveData<ServiceResult<List<Movie>>> loadPopularMovie() {
        //load from server
        loadPopularMoviesFromServer();

        return popularMoviesLiveData;
    }

    private void loadPopularMoviesFromServer() {
        executors.networkIO().execute(() -> popularMoviesLiveData.postValue(movieService.loadPopularMovie(1)));
    }

    @Override
    public void loadNextPagePopularMovie(final int page) {
        executors.networkIO().execute(() -> {
            ServiceResult<List<Movie>> result = movieService.loadPopularMovie(page);
            if (result.isSuccess()){
                ArrayList<Movie> newList = new ArrayList<>();

                newList.addAll(popularMoviesLiveData.getValue().data);
                newList.addAll(result.data);

                result.data = newList;
                popularMoviesLiveData.postValue(result);
            }
            else{
                popularMoviesLiveData.postValue(result); //I guess if something failed it will remove all items..
            }
        });
    }

    @Override
    public LiveData<ServiceResult<List<Movie>>> loadTopRateMovie() {
        executors.networkIO().execute(() -> {
            topRatedMoviesLIveData.postValue(movieService.loadTopRateMovie(1));
        });

        return topRatedMoviesLIveData;
    }

    @Override
    public void loadNextPageTopRate(int page) {
        executors.networkIO().execute(() -> {
            ServiceResult<List<Movie>> result = movieService.loadTopRateMovie(page);
            if (result.isSuccess()){
                ArrayList<Movie> newList = new ArrayList<>();

                newList.addAll(topRatedMoviesLIveData.getValue().data);
                newList.addAll(result.data);

                result.data = newList;
                topRatedMoviesLIveData.postValue(result);
            }
            else{
                topRatedMoviesLIveData.postValue(result); //I guess if something failed it will remove all items..
            }
        });
    }

    private MutableLiveData<Movie> _movieLiveData = new MutableLiveData<>();

    @Override
    public LiveData<Movie> loadMovie(Movie movie) {
        _movieLiveData  = new MutableLiveData<>();

        if (!_loadedFromDatabase(movie.getId())){
            _loadMovie(movie);
        }

        return _movieLiveData;
    }

    private void _loadMovie(Movie movie) {
            executors.networkIO().execute(() -> {
                ServiceResult<List<Review>> resultReview = movieService.loadReview(movie.getId());
                if (resultReview.isSuccess()) {
                    movie.setReviews(resultReview.data);
                }

                ServiceResult<List<Video>> resultVideo = movieService.loadVideo(movie.getId());
                if (resultVideo.isSuccess()) {
                    movie.setVideos(resultVideo.data);
                }

                Log.i(TAG, "_loadMovie80- : " + movie.getTitle() + " trailers-size=" + movie.getVideos().size() + " review-size=" + movie.getReviews().size());
                _movieLiveData.postValue(movie);
            });
    }

    private boolean _loadedFromDatabase(long id) {
        return false;
    }
}
