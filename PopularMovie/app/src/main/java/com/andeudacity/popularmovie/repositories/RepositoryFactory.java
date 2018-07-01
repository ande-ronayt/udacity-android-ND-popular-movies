package com.andeudacity.popularmovie.repositories;

import com.andeudacity.popularmovie.AppExecutors;
import com.andeudacity.popularmovie.database.MovieDatabase;
import com.andeudacity.popularmovie.services.IMovieService;

/**
 * Created by andrii on 4/29/18.
 */

public class RepositoryFactory {

    private final AppExecutors executors;

    public RepositoryFactory(AppExecutors executors){
        this.executors = executors;
    }

    private static IMovieRepository mMovieRepository;

    public synchronized IMovieRepository getMovieRepository(IMovieService movieService, MovieDatabase movieDatabase){
        if (mMovieRepository == null){
            createMovieRepository(movieService, movieDatabase);
        }

        return mMovieRepository;
    }

    private synchronized void createMovieRepository(IMovieService movieService, MovieDatabase movieDatabase) {
        if (mMovieRepository == null){
            mMovieRepository = new MovieRepository(executors, movieService, movieDatabase);
        }
    }
}
