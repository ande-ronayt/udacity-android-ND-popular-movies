package com.andeudacity.popularmovie.listadapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andeudacity.popularmovie.databinding.MovieListCellBinding;
import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.helpers.ImageHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrii on 4/30/18.
 */

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private final IOnCellClickListener mListener;

    private List<Movie> mMovies;

    public MovieRecyclerViewAdapter(IOnCellClickListener listener) {
        mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private final MovieListCellBinding mBinding;

        public ViewHolder(MovieListCellBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;

            binding.getRoot().setOnClickListener(view -> {
                if (mListener == null) return;

                int index = getAdapterPosition();
                Movie movie = mMovies.get(index);

                mListener.OnClick(movie);
            });
        }

        public void bind(Movie movie) {
            mBinding.setPosterPath(movie.getPosterImagePath());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieListCellBinding rowBinding = MovieListCellBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mMovies != null){
            holder.bind(mMovies.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    public void setItems(List<Movie> movies){
        if (mMovies == null){
            mMovies = new ArrayList<>();
            mMovies.addAll(movies);
            notifyItemRangeInserted(0, movies.size());
            //notifyDataSetChanged();
        }
        else{
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MovieListDiffUtilCallback(movies), true);

            this.mMovies = movies;

            result.dispatchUpdatesTo(this);
        }
    }

    class MovieListDiffUtilCallback extends DiffUtil.Callback{

        private final List<Movie> newList;

        public MovieListDiffUtilCallback(List<Movie> oldList){

            this.newList = oldList;
        }

        @Override
        public int getOldListSize() {
            return getItemCount();
        }

        @Override
        public int getNewListSize() {
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mMovies.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return compareMoviesContent(mMovies.get(oldItemPosition), newList.get(newItemPosition));
        }

        private boolean compareMoviesContent(Movie oldMovie, Movie newMovie) {
            return !(oldMovie.getId() != newMovie.getId() ||
                    !oldMovie.getTitle().equals(newMovie.getTitle()) ||
                    !oldMovie.getPosterImagePath().equals(newMovie.getPosterImagePath()) ||
                    !oldMovie.getPlotSynopsis().equals(newMovie.getPlotSynopsis()) ||
                    !oldMovie.getReleaseDate().equals(newMovie.getReleaseDate()) ||
                    oldMovie.getUserRating() != newMovie.getUserRating());
        }


    }

    public interface IOnCellClickListener {
        void OnClick(Movie movie);
    }
}
