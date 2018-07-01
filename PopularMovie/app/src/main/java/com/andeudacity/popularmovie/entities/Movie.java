package com.andeudacity.popularmovie.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.andeudacity.popularmovie.helpers.ImageHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrii on 4/29/18.
 */

@Entity
public class Movie implements Parcelable { // I generated it by www.parcelabler.com

    @PrimaryKey
    private long id;

    /**
     * original title
     */
    private String title;

    /**
     * movie poster image thumbnail
     * */
    private String poster_path;
    // for some reason I need it for Room
    public String getPoster_path() {return poster_path;}

    /**
     * A plot synopsis
     */
    private String overview;
    // for some reason I need it for Room
    public String getOverview() { return overview;}

    //release date
    private String release_date;
    // for some reason I need it for Room
    public String getRelease_date() { return release_date;}

    /**
     * user rating (called vote_average in the api)
     */
    private String vote_average;
    // for some reason I need it for Room
    public String getVote_average() { return vote_average;}

    /**
     * All trailors for movie
     */
    private List<Video> videos;

    /**
     * All reviews for movie
     */
    private List<Review> reviews;

    private boolean favourite = false;

    public Movie(long id, String title, String poster_path,
                 String overview, String release_date,
                 String vote_average, boolean favourite,
                 List<Video> videos, List<Review> reviews){
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.videos = videos;
        this.reviews = reviews;
        this.favourite = favourite;
    }


    /**
     * original title
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * movie poster image thumbnail
     */
    public String getPosterImagePath() {
        return ImageHelper.getImageFullUrl(poster_path);
    }

    public void setPosterImagePath(String image) {
        this.poster_path = image;
    }

    /**
     * user rating (called vote_average in the api)
     */
    public float getUserRating() {
        float result = 0;
        try{
            result = Float.parseFloat(vote_average);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setUserRating(String vote_average) {
        this.vote_average = vote_average;
    }

    /**
     * A plot synopsis
     */
    public String getPlotSynopsis() {
        return overview;
    }

    public void setPlotSynopsis(String overview) {
        this.overview = overview;
    }

    /**
     * release date
     */
    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(vote_average);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public List<Video> getVideos() {
        if (videos == null){
            videos = new ArrayList<>();
        }

        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Review> getReviews() {
        if (reviews == null){
            reviews = new ArrayList<>();
        }
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
