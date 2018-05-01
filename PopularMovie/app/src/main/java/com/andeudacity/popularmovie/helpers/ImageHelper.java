package com.andeudacity.popularmovie.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrii on 5/1/18.
 */

public class ImageHelper {

    private static final String imageBaseUrl = "http://image.tmdb.org/t/p/";

    private static String quality = "w342"; // current quality

    private final static List<String> qualityList; // in future can use it to change quality in settings

    static {
        qualityList = new ArrayList<>();
        qualityList.add("w92");
        qualityList.add("w154");
        qualityList.add("w185");
        qualityList.add("w342");
        qualityList.add("w500");
        qualityList.add("w780");
        qualityList.add("original");
    }


    public static String getQuality() {
        return quality;
    }

    public static void setQuality(String quality) {
        ImageHelper.quality = quality;
    }

    public static String getImageFullUrl(String imageUrl){
        return imageBaseUrl + quality + imageUrl;
    }
}
