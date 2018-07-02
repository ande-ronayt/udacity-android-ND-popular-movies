package com.andeudacity.popularmovie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.andeudacity.popularmovie.database.MovieDatabase;
import com.andeudacity.popularmovie.databinding.ActivityMainBinding;
import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.listadapters.MovieRecyclerViewAdapter;
import com.andeudacity.popularmovie.repositories.IMovieRepository;
import com.andeudacity.popularmovie.repositories.RepositoryFactory;
import com.andeudacity.popularmovie.services.ServiceFactory;
import com.andeudacity.popularmovie.viewModels.MovieListViewModel;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.IOnCellClickListener {

    //QUESTIONS:

    //1. How to keep list position after rotating ?


    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setVm(obtainViewModel());

        AppExecutors executors = ((MovieApplication)getApplication()).getExecutors();
        RepositoryFactory repoFactory = new RepositoryFactory(executors);
        ServiceFactory serviceFacotry = new ServiceFactory(getString(R.string.apiKey));
        MovieDatabase database = MovieDatabase.getInstance(this);
        IMovieRepository movieRepo = repoFactory.getMovieRepository(serviceFacotry.getMovieService(), database);

        mBinding.getVm().init(movieRepo);

        mBinding.getVm().getMoviesListMediator().observe(this, aVoid -> {
            //do nothing
        });

        setSupportActionBar(mBinding.toolbar);

        initList();
    }

    private void initList() {

        int columnCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            columnCount = 3;
        }

        final GridLayoutManager layoutManager = new GridLayoutManager(this, columnCount);
        mBinding.list.setLayoutManager(layoutManager);
        mBinding.list.setAdapter(new MovieRecyclerViewAdapter(this));

        mBinding.list.addOnScrollListener(mBinding.getVm().getScrollListener());
//        mBinding.list.scrollToPosition(mBinding.getVm().listPosition); //din't work.. items still null
    }

    private MovieListViewModel obtainViewModel() {
        return ViewModelProviders.of(this).get(MovieListViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuPopular:
                mBinding.getVm().showPopular();
                mBinding.toolbar.setTitle(R.string.title_popular_movies);
                break;
            case R.id.menuTopRated:
                mBinding.getVm().showTopRated();
                mBinding.toolbar.setTitle(R.string.title_top_rated_movies);
                break;
            case R.id.menuFavourite:
                mBinding.getVm().showFavourite();
                mBinding.toolbar.setTitle(R.string.title_favourite_movies);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnClick(Movie movie) {
        startActivity(MovieDetailActivity.getIntent(this, movie));
    }
}
