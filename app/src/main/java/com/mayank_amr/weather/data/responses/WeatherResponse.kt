package com.mayank_amr.weather.data.responses

data class WeatherResponse(
    val dt: Double,
    val id: Int,
    val main: Main,
    val name: String
) {
    data class Main(
        val temp: Double,
        val temp_max: Double,
        val temp_min: Double
    ){
        fun getCurrentTemp() = "${temp}\u2103"
    }
}