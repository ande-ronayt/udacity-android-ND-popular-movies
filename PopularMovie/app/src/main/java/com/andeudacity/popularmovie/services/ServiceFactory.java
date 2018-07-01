package com.andeudacity.popularmovie.services;

/**
 * Created by andrii on 4/29/18.
 */

public class ServiceFactory {

    private final String apiKey;

    public ServiceFactory(String apiKey){
        this.apiKey = apiKey;
    }

    public IMovieService getMovieService(){
        return new MovieService(apiKey);
    }
}
