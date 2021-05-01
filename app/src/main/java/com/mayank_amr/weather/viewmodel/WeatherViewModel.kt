package com.mayank_amr.weather.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
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
    private val _forecast: MutableLiveData<Resource<ForecastResponse>> = MutableLiveData()

    val currentTemp: LiveData<Resource<ForecastResponse>>
        get() = _forecast


    fun loaddata() = viewModelScope.launch {
        _forecast.value = Resource.Loading
        _forecast.value = repository.currentTemp(
            "26.328864",
            "81.599943",
            "32",
            "9b8cb8c7f11c077f8c4e217974d9ee40"
        )
    }

    init {
        Log.d(TAG, ":ViewModel Created")

        viewModelScope.launch {
            _forecast.value = Resource.Loading
            _forecast.value = repository.currentTemp(
                "26.328864",
                "81.599943",
                "40",
                "9b8cb8c7f11c077f8c4e217974d9ee40"
            )
        }
    }

}