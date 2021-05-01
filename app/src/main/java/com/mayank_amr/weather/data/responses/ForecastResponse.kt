package com.mayank_amr.weather.data.responses

import java.util.*


data class ForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<TimeStamps>,
    val message: Int
) {
    data class City(
        //val coord: Coord,
        val country: String,
        val id: Int,
        val name: String
        //val population: Int,
        //val sunrise: Int,
        //val sunset: Int,
        //val timezone: Int
    ) {
        data class Coord(
            val lat: Double,
            val lon: Double
        )
    }

    data class TimeStamps(
        val dt: Long,
        val dt_txt: String,
        val main: Main
    ) {
        data class Main(
            //val feels_like: Double,
            //val grnd_level: Int,
            //val humidity: Int,
            //val pressure: Int,
            //val sea_level: Int,
            val temp: Double,
            val temp_kf: Double,
            val temp_max: Double,
            val temp_min: Double
        ) {
            fun getTemperature() = "${temp}\u2103"
        }

        fun getDayName(): String {
            //Get instance of calendar
            val calendar = Calendar.getInstance(Locale.getDefault())
            //get current date from ts
            calendar.timeInMillis = dt * 1000L
            //return formatted date
            return android.text.format.DateFormat.format("EEEE", calendar).toString()
        }


    }


}