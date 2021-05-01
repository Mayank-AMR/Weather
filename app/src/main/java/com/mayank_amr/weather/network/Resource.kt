package com.mayank_amr.weather.network

import okhttp3.ResponseBody

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 01-05-2021 12:06 PM
 */
sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()

    object Loading : Resource<Nothing>()
}