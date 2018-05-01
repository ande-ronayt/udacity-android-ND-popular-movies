package com.andeudacity.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andeudacity.popularmovie.databinding.ActivityMainBinding;
import com.andeudacity.popularmovie.databinding.ActivityMovieDetailBinding;
import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.helpers.ImageHelper;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding mBinding;
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movie = getMovie();
        if (movie == null){
            this.finish();  //nothing to show
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        mBinding.setVm(movie);

        getSupportActionBar().setTitle(movie.getTitle());
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
}
