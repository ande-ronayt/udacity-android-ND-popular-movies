package com.andeudacity.popularmovie;

import android.app.Application;

/**
 * Created by andrii on 4/30/18.
 */

public class MovieApplication extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppExecutors getExecutors(){
        if (mAppExecutors == null){ // I still confused how Android OS works.. I've read that sometimes it can delete objects
            mAppExecutors = new AppExecutors();
        }

        return mAppExecutors;
    }
}
