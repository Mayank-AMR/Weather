package com.mayank_amr.weather.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mayank_amr.weather.data.LocationPreferences
import com.mayank_amr.weather.network.RemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 01-05-2021 12:32 PM
 */
abstract class BaseFragment<VM : BaseViewModel, B : ViewDataBinding, BR : BaseRepository> :
    Fragment() {

    protected val remoteDataSource = RemoteDataSource()
    protected lateinit var binding: B
    protected lateinit var viewmodel: VM

    protected lateinit var locationPreferences: LocationPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        locationPreferences = LocationPreferences(requireContext())
        val factory = ViewModelFactory(getFragmentRepository())
        viewmodel = ViewModelProvider(this, factory).get(getViewModel())

        /*
         * It will load the location data in main memory when fragment create and whenever need it will
         * deliver quickly that will not create ANR.
         */
        lifecycleScope.launch {
            locationPreferences.latitude.first()
            locationPreferences.longitude.first()
        }

        return inflater.inflate(layoutId(), container, false)
    }


    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentRepository(): BR

    abstract fun layoutId(): Int


}