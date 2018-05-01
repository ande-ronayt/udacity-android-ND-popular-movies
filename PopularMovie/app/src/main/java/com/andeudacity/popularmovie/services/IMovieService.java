package com.andeudacity.popularmovie.services;

import com.andeudacity.popularmovie.entities.Movie;

import java.util.List;

/**
 * Created by andrii on 4/29/18.
 */

public interface IMovieService {
    ServiceResult<List<Movie>> loadPopularMovie(int page);

    ServiceResult<List<Movie>> loadTopRateMovie(int page);
}
