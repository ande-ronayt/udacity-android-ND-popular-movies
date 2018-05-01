package com.andeudacity.popularmovie.services.dto;

import com.andeudacity.popularmovie.entities.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrii on 4/30/18.
 */

public class MovieListResponseDto {
    private int page;

    private int total_results;

    private int total_pages;

    private ArrayList<Movie> results;

    public MovieListResponseDto(){
        results = new ArrayList<>();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
