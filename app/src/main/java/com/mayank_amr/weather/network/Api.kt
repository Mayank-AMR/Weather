package com.mayank_amr.weather.network

import com.mayank_amr.weather.data.responses.ForecastResponse
import retrofit2.http.*

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 01-05-2021 11:50 AM
 */
interface Api {
    @GET("/data/2.5/forecast")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("cnt") count:String,
        @Query("units") units: String,
        @Query("APPID") appid: String

    ): ForecastResponse
}