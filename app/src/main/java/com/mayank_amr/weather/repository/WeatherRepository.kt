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

    suspend fun currentTemp(lat: String, lon: String, cnt: String, appid: String) =
        safeApiCall { api.getCurrentWeather(lat, lon, cnt, appid) }
}