package com.andeudacity.popularmovie.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.andeudacity.popularmovie.entities.Movie;

@Database( entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(MovieListsConverter.class)
public abstract class MovieDatabase extends RoomDatabase{

    private static final String TAG = "MovieDatabase";
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popular_movie";
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, DATABASE_NAME)
                        .build();
            }
        }

        Log.d(TAG, "Getting databse instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
