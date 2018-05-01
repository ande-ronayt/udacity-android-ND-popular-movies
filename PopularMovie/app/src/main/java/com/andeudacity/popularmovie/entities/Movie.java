package com.andeudacity.popularmovie.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.andeudacity.popularmovie.helpers.ImageHelper;

/**
 * Created by andrii on 4/29/18.
 */

public class Movie implements Parcelable { // I generated it by www.parcelabler.com

    private long id;

    /**
     * original title
     */
    private String title;

    /**
     * movie poster image thumbnail
     * */
    private String poster_path;


    /**
     * A plot synopsis
     */
    private String overview;

    //release date
    private String release_date;


    /**
     * user rating (called vote_average in the api)
     */
    private String vote_average;

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
}
