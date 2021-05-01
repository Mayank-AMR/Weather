package com.mayank_amr.weather.base

import androidx.lifecycle.ViewModel

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 01-05-2021 12:50 PM
 */
abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

}