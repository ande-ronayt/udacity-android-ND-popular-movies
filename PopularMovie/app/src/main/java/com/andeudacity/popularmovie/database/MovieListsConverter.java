package com.andeudacity.popularmovie.database;

import android.arch.persistence.room.TypeConverter;

import com.andeudacity.popularmovie.entities.Review;
import com.andeudacity.popularmovie.entities.Video;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieListsConverter {

    @TypeConverter
    public static List<Video> toVideoList(String json){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Video[] videoArray = gson.fromJson(json, Video[].class);

        return new ArrayList<>(Arrays.asList(videoArray));
    }

    @TypeConverter
    public static String toVideoJson(List<Video> list) {
        return toJson(list);
    }


    @TypeConverter
    public static List<Review> toReviewList(String json){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Review[] videoArray = gson.fromJson(json, Review[].class);

        return new ArrayList<>(Arrays.asList(videoArray));
    }

    @TypeConverter
    public static String toReviewJson(List<Review> list) {
        return toJson(list);
    }

    private static String toJson(Object list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list);

        return json;
    }
}
