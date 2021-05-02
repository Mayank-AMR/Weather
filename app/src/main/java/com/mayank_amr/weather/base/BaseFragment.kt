package com.mayank_amr.weather.base

import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mayank_amr.weather.network.RemoteDataSource

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 01-05-2021 12:32 PM
 */
abstract class BaseFragment<VM : BaseViewModel, B : ViewDataBinding, BR : BaseRepository> :
    Fragment() {

    protected val remoteDataSource = RemoteDataSource()
    protected lateinit var binding: B
    protected lateinit var viewmodel: VM



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = ViewModelFactory(getFragmentRepository())
        viewmodel = ViewModelProvider(this, factory).get(getViewModel())


        return inflater.inflate(layoutId(), container, false)
    }


    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentRepository(): BR

    abstract fun layoutId(): Int


}