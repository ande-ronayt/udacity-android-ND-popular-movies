package com.andeudacity.popularmovie;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.andeudacity.popularmovie.database.MovieDatabase;
import com.andeudacity.popularmovie.databinding.ActivityMainBinding;
import com.andeudacity.popularmovie.databinding.ActivityMovieDetailBinding;
import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.entities.Review;
import com.andeudacity.popularmovie.entities.Video;
import com.andeudacity.popularmovie.helpers.ImageHelper;
import com.andeudacity.popularmovie.repositories.IMovieRepository;
import com.andeudacity.popularmovie.repositories.RepositoryFactory;
import com.andeudacity.popularmovie.services.ServiceFactory;
import com.andeudacity.popularmovie.viewModels.DetailViewModel;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private String TAG = "MovieDetail";

    private ActivityMovieDetailBinding mBinding;
    private Movie movie;

    private DetailViewModel vm;

    private Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movie = getMovie();
        if (movie == null){
            this.finish();  //nothing to show
        }

        AppExecutors executors = ((MovieApplication)getApplication()).getExecutors();
        RepositoryFactory repoFactory = new RepositoryFactory(executors);
        ServiceFactory serviceFacotry = new ServiceFactory(getString(R.string.apiKey));
        MovieDatabase database = MovieDatabase.getInstance(this);
        IMovieRepository movieRepo = repoFactory.getMovieRepository(serviceFacotry.getMovieService(), database);

        vm = obtainViewModel();
        vm.init(movieRepo, movie);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        mBinding.setVm(movie);

        subscribe();

        getSupportActionBar().setTitle(movie.getTitle());

        addButton = findViewById(R.id.btnAddToFavourite);

        addButton.setOnClickListener((btn) -> {
            if (!movie.isFavourite()){
                addMovie(movie);
            }
            else{
                removeMovie(movie);
            }
        });
    }

    private void removeMovie(Movie movie) {
        AppExecutors executors = ((MovieApplication)getApplication()).getExecutors();
        executors.diskIO().execute(() ->{
            MovieDatabase.getInstance(this).movieDao().deleteById(movie.getId());
            movie.setFavourite(false);
            //            MovieDatabase.getInstance(this).movieDao().deleteMovie(movie);

            mBinding.setVm(movie);
        });
    }

    private void addMovie(Movie movie) {
        AppExecutors executors = ((MovieApplication)getApplication()).getExecutors();
        executors.diskIO().execute(() ->{
            movie.setFavourite(true);
            MovieDatabase.getInstance(this).movieDao().insertMovie(movie);
            mBinding.setVm(movie);
        });
    }

    private void subscribe() {
        vm.getMovieLiveData().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                MovieDetailActivity.this.movie = movie;
                vm.getMovieLiveData().removeObserver(this);

                setModel(movie);

                addButton.setVisibility(View.VISIBLE);
                //todo show button
            }
        });
    }

    private void setModel(Movie movie) {
        Log.i(TAG, "setModel: " + movie.getTitle() + " trailers-size=" + movie.getVideos().size() + " review-size=" + movie.getReviews().size());
        mBinding.setVm(movie);
        showTrailers(movie);
        showReviews(movie);

    }

    private void showReviews(Movie movie) {
        List<Review> reviews = movie.getReviews();

        for(int i = 0; i < reviews.size(); i++){

        }
    }

    private void showTrailers(Movie movie) {
        List<Video> trailers = movie.getVideos();
        ViewGroup container = findViewById(R.id.llTrailers);
        for(int i = 0; i < trailers.size(); i++){
            Video trailer = trailers.get(i);
            container.addView(createTrailerLine(trailer.getName(), trailer.getKey()));
        }
    }

    private DetailViewModel obtainViewModel() {
        return ViewModelProviders.of(this).get(DetailViewModel.class);
    }

    private Movie getMovie() {

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_REFERRER)){
            return intent.getParcelableExtra(Intent.EXTRA_REFERRER);
        }

        return null;
    }

    public static Intent getIntent(Context context, Movie movie){

        Intent intent = new Intent(context, MovieDetailActivity.class);

        intent.putExtra(Intent.EXTRA_REFERRER, movie);

        return intent;

    }

    private View createTrailerLine(String name, String link){

        TextView textView = new TextView(this);
        textView.setText(name);

        return textView;
    }

    private View createReviewLine(String author, String content){
        return null;
    }
}
