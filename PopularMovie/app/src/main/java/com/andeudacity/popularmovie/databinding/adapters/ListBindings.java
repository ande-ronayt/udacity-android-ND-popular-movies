package com.andeudacity.popularmovie.databinding.adapters;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.andeudacity.popularmovie.R;
import com.andeudacity.popularmovie.entities.Movie;
import com.andeudacity.popularmovie.listadapters.MovieRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by andrii on 4/30/18.
 */

public class ListBindings {
    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Movie> movies){
        MovieRecyclerViewAdapter adapter = (MovieRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter != null){
            adapter.setItems(movies);
        }
    }

    @BindingAdapter("app:PicassoSrc")
    public static void setImage(ImageView image,String url){

        Picasso.with(image.getContext())
                .load(url)
                .placeholder(R.drawable.ic_local_movies)
                .error(R.drawable.ic_error)
                .into(image);

    }
}
