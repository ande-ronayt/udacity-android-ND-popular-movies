package com.andeudacity.popularmovie.services;

import android.support.annotation.NonNull;

/**
 * Created by andrii on 4/29/18.
 */

public class ServiceResult<T> {
    private static final int STATUS_SUCCESS = 0;
    private static final int STATUS_ERROR = 1;

    private final int status;
    public T data;
    public final String message;

    private ServiceResult(int status, T data, String message){
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ServiceResult<T> success(T data){
        return new ServiceResult<>(STATUS_SUCCESS, data, null);
    }

    public static <T> ServiceResult<T> error(String message, T data){
        return new ServiceResult<>(STATUS_ERROR, data, message);
    }

    public boolean isSuccess() {
        return status == STATUS_SUCCESS;
    }
}
