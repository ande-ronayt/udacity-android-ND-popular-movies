package com.andeudacity.popularmovie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.andeudacity.popularmovie.database.MovieDatabase;
import com.andeudacity.popularmovie.databinding.ActivityMainBinding;
import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.listadapters.MovieRecyclerViewAdapter;
import com.andeudacity.popularmovie.repositories.IMovieRepository;
import com.andeudacity.popularmovie.repositories.RepositoryFactory;
import com.andeudacity.popularmovie.services.ServiceFactory;
import com.andeudacity.popularmovie.viewModels.MovieListViewModel;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.IOnCellClickListener {

    Parcelable mListState = null;

    private ActivityMainBinding mBinding;
    private static String LIST_STATE_KEY = "LIST_STATE";
    private MovieRecyclerViewAdapter mAdapter;
    private MovieListViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initList();

        vm = obtainViewModel();

        mBinding.setVm(vm);

        mBinding.list.addOnScrollListener(mBinding.getVm().getScrollListener());

        AppExecutors executors = ((MovieApplication)getApplication()).getExecutors();
        RepositoryFactory repoFactory = new RepositoryFactory(executors);
        ServiceFactory serviceFacotry = new ServiceFactory(getString(R.string.apiKey));
        MovieDatabase database = MovieDatabase.getInstance(this);
        IMovieRepository movieRepo = repoFactory.getMovieRepository(serviceFacotry.getMovieService(), database);

        vm.init(movieRepo);

        vm.getMoviesListMediator().observe(this, aVoid -> {
            //do nothing
        });

        setSupportActionBar(mBinding.toolbar);


    }

    private void initList() {

        int columnCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            columnCount = 3;
        }

        final GridLayoutManager layoutManager = new GridLayoutManager(this, columnCount);
        mBinding.list.setLayoutManager(layoutManager);
        mAdapter = new MovieRecyclerViewAdapter(this);
        mBinding.list.setAdapter(mAdapter);


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
                vm.showPopular();
                mBinding.toolbar.setTitle(R.string.title_popular_movies);
                break;
            case R.id.menuTopRated:
                vm.showTopRated();
                mBinding.toolbar.setTitle(R.string.title_top_rated_movies);
                break;
            case R.id.menuFavourite:
                vm.showFavourite();
                mBinding.toolbar.setTitle(R.string.title_favourite_movies);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnClick(Movie movie) {
        startActivity(MovieDetailActivity.getIntent(this, movie));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save list state
        mListState =  mBinding.list.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
    }

    //Restore state in the onRestoreInstanceState():

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve list state and list/item positions
        if(savedInstanceState != null){
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            mAdapter.setItems(vm.getMovies().get());
//            mBinding.list.getLayoutManager().onRestoreInstanceState(mListState);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.list.getLayoutManager().onRestoreInstanceState(mListState);
                }
            }, 200);
        }
    }
}
