package com.mayank_amr.weather.repository

import com.mayank_amr.weather.base.BaseRepository
import com.mayank_amr.weather.network.Api

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 01-05-2021 01:11 PM
 */
class WeatherRepository(
    private val api: Api
) : BaseRepository() {

    private val NUMBER_OF_TIME_STAMP = "40"
    private val TEMPERATURE_UNIT = "metric"
    private val APP_ID = "9b8cb8c7f11c077f8c4e217974d9ee40"

    suspend fun currentWeather(lat: String, lon: String) =
        safeApiCall { api.getCurrentWeather(lat, lon, TEMPERATURE_UNIT, APP_ID) }

    suspend fun forecastWeather(lat: String, lon: String) =
        safeApiCall {
            api.getForecastWeather(
                lat,
                lon,
                NUMBER_OF_TIME_STAMP,
                TEMPERATURE_UNIT,
                APP_ID
            )
        }


}