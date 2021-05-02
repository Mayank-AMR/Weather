package com.mayank_amr.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mayank_amr.weather.base.BaseViewModel
import com.mayank_amr.weather.data.responses.ForecastResponse
import com.mayank_amr.weather.data.responses.WeatherResponse
import com.mayank_amr.weather.network.Resource
import com.mayank_amr.weather.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : BaseViewModel(repository) {
    private var _currentWeather: MutableLiveData<Resource<WeatherResponse>> = MutableLiveData()

    private var _forecastWeather: MutableLiveData<Resource<ForecastResponse>> = MutableLiveData()


    val currentWeather: LiveData<Resource<WeatherResponse>>
        get() = _currentWeather

    val forecastWeather: LiveData<Resource<ForecastResponse>>
        get() = _forecastWeather


    fun fetchWeatherData(lat: String, lon: String) = viewModelScope.launch {
        _currentWeather.value = Resource.Loading
        _currentWeather.value = repository.currentWeather(lat, lon)

    }

    fun fetchForecastData(lat: String, lon: String) = viewModelScope.launch {
        _forecastWeather.value = Resource.Loading
        _forecastWeather.value = repository.forecastWeather(
            lat,
            lon,
        )
    }


}