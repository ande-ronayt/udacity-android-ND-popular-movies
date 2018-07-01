package com.andeudacity.popularmovie.services.dto;

import com.andeudacity.popularmovie.entities.Review;
import com.andeudacity.popularmovie.entities.Video;

import java.util.ArrayList;
import java.util.List;

public class AdditionalInfo {

    private List<Review> reviews;
    private List<Video> videos;

    public AdditionalInfo(){
        reviews = new ArrayList<>();
        videos = new ArrayList<>();
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
