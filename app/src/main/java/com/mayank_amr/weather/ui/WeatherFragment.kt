package com.mayank_amr.weather.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import org.json.JSONObject

class WeatherFragment :
    BaseFragment<WeatherViewModel, WeatherFragmentBinding, WeatherRepository>() {


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.weather_fragment, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding = DataBindingUtil.bind<WeatherFragmentBinding>(view)!!
        binding.apply {
//            binding.button.setOnClickListener {
//            }

        }

        viewmodel.currentTemp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.textView.text = it.value.list[0].dt_txt//.progressBarPostLoad.visible(false)

                    Log.d(TAG, "onViewCreated: Success")


                    //updateUI(it.value)
                }
                is Resource.Loading -> {
                    //binding!!.progressBarPostLoad.visible(true)
                    binding!!.textView.text = "Loading"
                    Log.d(TAG, "onViewCreated: Loading")

                }

                is Resource.Failure -> {
                    //binding!!.progressBarPostLoad.visible(false)
                    binding!!.textView.text = "Failed"
                    Log.d(TAG, "onViewCreated: Failed")



                }
            }
        })


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