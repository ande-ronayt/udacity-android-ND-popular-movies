package com.andeudacity.popularmovie.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.widget.Toast;

import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.repositories.IMovieRepository;
import com.andeudacity.popularmovie.services.ServiceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrii on 4/29/18.
 */

public class MovieListViewModel extends AndroidViewModel {


    public final static int TYPE_POPULAR = 1;
    public final static int TYPE_TOP_RATE = 2;

    private Boolean firstInit = true;

    private int type = TYPE_POPULAR;

    private int currentPage;

    private boolean isLoadingNextPage = false;

    private IMovieRepository movieRepository;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
    }


    private final ObservableField<List<Movie>> mMovies = new ObservableField<>();//this list goes to view

    private MediatorLiveData<Void> moviesListMediator; // this is mediator, will change mMovies, in activity will do simple subscribe to it
    private LiveData<ServiceResult<List<Movie>>> moviesLiveData; //this is coming from repository
    // ^ I don't really like this approach, but don't know how to do it better

    public void init(IMovieRepository movieRepository){
        if (!firstInit){
            return;
        }

        mMovies.set(new ArrayList<>());

        currentPage = 1;
        this.movieRepository = movieRepository;

        moviesLiveData = movieRepository.loadPopularMovie();

        moviesListMediator = new MediatorLiveData<>();
        addSourceToMediator();


        firstInit = false;
    }

    private void addSourceToMediator() {
        moviesListMediator.addSource(moviesLiveData, sr -> {
            if (sr != null) {
                isLoadingNextPage = false;
                if (!sr.isSuccess()) {
                    showToast(sr.message);
                } else {
                    mMovies.set(sr.data);
                }
            }
        });
    }

    public LiveData<Void> getMoviesListMediator(){
        return moviesListMediator;
    }

    public ObservableField<List<Movie>> getMovies(){
        return mMovies;
    }

    public void showPopular(){

        moviesListMediator.removeSource(moviesLiveData);

        type = TYPE_POPULAR;
        currentPage = 1;
        moviesLiveData = movieRepository.loadPopularMovie();

        addSourceToMediator();
    }

    public void showTopRated(){

        moviesListMediator.removeSource(moviesLiveData);

        type = TYPE_TOP_RATE;
        currentPage = 1;
        moviesLiveData = movieRepository.loadTopRateMovie();

        addSourceToMediator();
    }

    private void loadNewPage(){
        isLoadingNextPage = true;
        switch (type){
            case TYPE_POPULAR:
                currentPage++;
                this.movieRepository.loadNextPagePopularMovie(currentPage);
                break;
            case TYPE_TOP_RATE:
                currentPage++;
                this.movieRepository.loadNextPageTopRate(currentPage);
                break;
        }
    }

    private void showToast(String message) {
        Context context = getApplication();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public RecyclerView.OnScrollListener getScrollListener() {
        return new OnScrollListener();
    }

    public int listPosition;

    public class OnScrollListener extends RecyclerView.OnScrollListener{

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (isLoadingNextPage)
                return;

            if (!recyclerView.canScrollVertically(1)) {
                if (!isLoadingNextPage){
                    loadNewPage();
                }
            }
        }


        // below approach had some problems:
        //so, it's loading in the beginning two times, also when rotation happens

//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            if (isLoadingNextPage)
//                return;
//
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//
//            int visibleItemCount = layoutManager.getChildCount();
//            int totalItemCount = layoutManager.getItemCount();
//            int pastVisibleItems = ((GridLayoutManager)layoutManager).findFirstVisibleItemPosition();
//
//
//            if (pastVisibleItems + visibleItemCount >= totalItemCount - 6) { // start loading before last 6 items loaded
//                if (!isLoadingNextPage){
//                    loadNewPage();
//                }
//            }
//
//        }

        //this one will try for saving list position:

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            listPosition = ((GridLayoutManager)layoutManager).findFirstVisibleItemPosition();
        }
    }
}
