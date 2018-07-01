package com.andeudacity.popularmovie.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.repositories.IMovieRepository;

public class DetailViewModel extends AndroidViewModel {
    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    private boolean _isFirstInit = true;

    public ObservableBoolean isVideoLoaded = new ObservableBoolean(false);

    public ObservableBoolean isReviewLoaded = new ObservableBoolean(false);

    private LiveData<Movie> movieLiveData = null;

    public void init(IMovieRepository repo, Movie movie){
        if (_isFirstInit){
            movieLiveData = repo.loadMovie(movie);
        }

        _isFirstInit = false;
    }

    public LiveData<Movie> getMovieLiveData() {
        return movieLiveData;
    }
}
