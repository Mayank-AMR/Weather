package com.mayank_amr.weather.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayank_amr.weather.repository.WeatherRepository
import com.mayank_amr.weather.viewmodel.WeatherViewModel

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 01-05-2021 01:09 PM
 */

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WeatherViewModel::class.java) -> WeatherViewModel(repository as WeatherRepository) as T

            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}
