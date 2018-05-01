package com.andeudacity.popularmovie.repositories;

import android.arch.lifecycle.LiveData;

import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.services.ServiceResult;

import java.util.List;

/**
 * Created by andrii on 4/29/18.
 */

public interface IMovieRepository {
    LiveData<ServiceResult<List<Movie>>> loadPopularMovie();
    void loadNextPagePopularMovie(int page);

    LiveData<ServiceResult<List<Movie>>> loadTopRateMovie();
    void loadNextPageTopRate(int page);
}
