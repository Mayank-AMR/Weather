package com.mayank_amr.weather.ui

import android.Manifest
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.mayank_amr.weather.R
import com.mayank_amr.weather.base.BaseFragment
import com.mayank_amr.weather.databinding.WeatherFragmentBinding
import com.mayank_amr.weather.network.Api
import com.mayank_amr.weather.network.Resource
import com.mayank_amr.weather.repository.WeatherRepository
import com.mayank_amr.weather.util.slideUp
import com.mayank_amr.weather.util.visible
import com.mayank_amr.weather.viewmodel.WeatherViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class WeatherFragment : BaseFragment<WeatherViewModel, WeatherFragmentBinding, WeatherRepository>() {

    private val TAG = "WeatherFragment"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_CODE = 123


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        checkPermission()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding = DataBindingUtil.bind<WeatherFragmentBinding>(view)!!

        viewmodel.currentWeather.observe(viewLifecycleOwner, {
            binding.progressBar.visible(it is Resource.Loading)
            binding.buttonRetry.visible(it is Resource.Failure)
            when (it) {
                is Resource.Success -> {
                    binding.currentTemperature = it.value
                    binding.llWeather.visible(true)
                    binding.scrollview.slideUp()
                }
                is Resource.Loading -> {

                }

                is Resource.Failure -> {
                    binding.apply {
                        constraintLayout.setBackgroundColor(Color.parseColor("#E85959"));
                        tvError.visible(true)
                        buttonRetry.visible(true)
                        buttonRetry.setOnClickListener {
                            val latitude = runBlocking { locationPreferences.latitude.first() }
                            val longitude = runBlocking { locationPreferences.longitude.first() }

                            if (latitude != null && longitude != null) {
                                getWeatherData(
                                    latitude,
                                    longitude
                                )
                                getForecastData(
                                    latitude,
                                    longitude
                                )
                            }
                        }
                    }
                }
            }
        })

        viewmodel.forecastWeather.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.forecastTemperature = it.value
                }
                is Resource.Loading -> {

                }

                is Resource.Failure -> {
                    binding.apply {
                        if (buttonRetry.isVisible){
                            buttonRetry.visible(false)
                        }
                    }
                }
            }
        })
    }


    override fun getViewModel() = WeatherViewModel::class.java

    override fun getFragmentRepository(): WeatherRepository {
        val api = remoteDataSource.buildApi(Api::class.java)
        return WeatherRepository(api)
    }

    override fun layoutId() = R.layout.weather_fragment


    private fun getWeatherData(lat: String, lon: String) {
        viewmodel.fetchWeatherData(lat, lon)
    }

    private fun getForecastData(lat: String, lon: String) {
        viewmodel.fetchForecastData(lat, lon)
    }


    private fun checkPermission(): Boolean {
        return if (viewLifecycleOwner.let {
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            viewLifecycleOwner.let {
                ActivityCompat.requestPermissions(
                    requireContext() as Activity, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), LOCATION_PERMISSION_CODE
                )
            }
            false
        }
    }

    private fun requestLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 60000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    Log.d(TAG, "onLocationResult: location result null.")
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {

                        lifecycleScope.launch {
                            locationPreferences.saveUserLocation(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                        }

                        getWeatherData(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                        getForecastData(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            }
        }
        if (checkPermission()) {
            LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
        }
    }

    private fun checkGps(): Boolean {
        val manager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    private fun requestToEnableGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onStart() {
        super.onStart()
        if (checkPermission()) {
            if (checkGps()) {
                requestLocation()
            } else {
                requestToEnableGps()
            }
        }
    }

}


