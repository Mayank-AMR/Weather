package com.mayank_amr.weather.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.mayank_amr.weather.R
import com.mayank_amr.weather.base.BaseFragment
import com.mayank_amr.weather.databinding.WeatherFragmentBinding
import com.mayank_amr.weather.network.Api
import com.mayank_amr.weather.network.Resource
import com.mayank_amr.weather.repository.WeatherRepository
import com.mayank_amr.weather.viewmodel.WeatherViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*

class WeatherFragment :
    BaseFragment<WeatherViewModel, WeatherFragmentBinding, WeatherRepository>() {


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = DataBindingUtil.bind<WeatherFragmentBinding>(view)!!
        binding.apply {

        }

        viewmodel.currentTemp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.data = it.value
//                    binding.tvDayOneTemp.text = "${it.value.list[8].main.temp}\u2103"
//                    binding.tvDayTwoTemp.text = "${it.value.list[16].main.temp}\u2103"
//                    binding.tvDayThreeTemp.text = "${it.value.list[24].main.temp}\u2103"
//                    binding.tvDayFourTemp.text = "${it.value.list[32].main.temp}\u2103"

                    binding.tvDayFourName.text = getShortDate(it.value.list[8].dt * 1000L)






                    Log.d(TAG, "onViewCreated: Success")
                    //updateUI(it.value)
                }
                is Resource.Loading -> {
                    //binding!!.progressBarPostLoad.visible(true)
                    //binding!!.tvDayOneTemp.text = it.value
                    Log.d(TAG, "onViewCreated: Loading")

                }

                is Resource.Failure -> {
                    //binding!!.progressBarPostLoad.visible(false)
                    //binding!!.textView.text = "Failed"
                    Log.d(TAG, "onViewCreated: Failed")


                }
            }
        })


    }

    fun getShortDate(ts: Long?): String {
        if (ts == null) return ""
        //Get instance of calendar
        val calendar = Calendar.getInstance(Locale.getDefault())
        //get current date from ts
        calendar.timeInMillis = ts
        //return formatted date
        return android.text.format.DateFormat.format("EEEE, MMM dd yyyy", calendar).toString()
    }

    override fun getViewModel() = WeatherViewModel::class.java

    override fun getFragmentRepository(): WeatherRepository {
        val lat = runBlocking {
            locationPreferences.latitude.first()
        }
        val lon = runBlocking {
            locationPreferences.longitude.first()
        }
        val api = remoteDataSource.buildApi(Api::class.java)
        return WeatherRepository(api)
    }

    override fun layoutId() = R.layout.weather_fragment

}