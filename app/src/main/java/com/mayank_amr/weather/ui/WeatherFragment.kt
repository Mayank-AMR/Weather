package com.mayank_amr.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.mayank_amr.weather.R
import com.mayank_amr.weather.base.BaseFragment
import com.mayank_amr.weather.databinding.WeatherFragmentBinding
import com.mayank_amr.weather.network.Api
import com.mayank_amr.weather.network.Resource
import com.mayank_amr.weather.repository.WeatherRepository
import com.mayank_amr.weather.viewmodel.WeatherViewModel


class WeatherFragment :
    BaseFragment<WeatherViewModel, WeatherFragmentBinding, WeatherRepository>() {

    private val TAG = "WeatherFragment"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_CODE = 123


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        checkPermission()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


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


    override fun getViewModel() = WeatherViewModel::class.java

    override fun getFragmentRepository(): WeatherRepository {
        val api = remoteDataSource.buildApi(Api::class.java)
        return WeatherRepository(api)
    }

    override fun layoutId() = R.layout.weather_fragment

    private fun obtainLocation() {
        Log.d(TAG, "obtainLocation")
        // get the last location
        if (checkPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d(TAG, "obtainLocation: Successfull")
//                 weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&key=" + api_id1
                getWeatherData(location!!.latitude.toString(), location!!.longitude.toString())
            }
        }
    }

    private fun getWeatherData(lat: String, lon: String) {
        viewmodel.loaddata(lat, lon)
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
        Log.d(TAG, "requestLocation: ")
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
                        Log.d(TAG, "onLocationResult: going to fetch weather data")
                        viewmodel.loaddata(location.latitude.toString(), location.longitude.toString())
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

    override fun onResume() {
        super.onResume()
    }
}


