package com.mayank_amr.weather.network

import com.mayank_amr.weather.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import okhttp3.OkHttpClient

import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 01-05-2021 12:09 PM
 */
class RemoteDataSource {
    companion object{
        private const val BASE_URL = "https://api.openweathermap.org/"
    }

    fun <Api> buildApi(
        api: Class<Api>
    ): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder().also { client ->
                        if (BuildConfig.DEBUG) {
                            val logging = HttpLoggingInterceptor()
                            //logging.level = (HttpLoggingInterceptor.Level.BASIC)
                            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                            client.addInterceptor(logging) }
                    }.build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}