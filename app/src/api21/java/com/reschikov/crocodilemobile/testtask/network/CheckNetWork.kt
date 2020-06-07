package com.reschikov.crocodilemobile.testtask.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import com.reschikov.crocodilemobile.testtask.R

class CheckNetWork(context: Context) : LiveData<String?>() {

    private val connectivity : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val strNoNetWork : String = context.getString(R.string.err_no_network)

    private val networkCallback = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.i("TAG onAvailable", "onAvailable")
            postValue(null)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.i("TAG onLost", "onLost")
            postValue(strNoNetWork)
        }
    }

    override fun onActive() {
        super.onActive()
        startOff()
        Log.i("TAG onActive", "onActive")
    }

    private fun startOff(){
        getStateNetWork()
        connectivity.registerNetworkCallback(createNetworkRequest(), networkCallback)
    }

    private fun getStateNetWork(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (connectivity.activeNetwork != null) {
                postValue(null)
                return
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
             if (connectivity.isDefaultNetworkActive){
                 postValue(null)
                 return
             }
        }
        postValue(strNoNetWork)
    }

    private fun createNetworkRequest() : NetworkRequest{
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
            .build()
    }

    override fun onInactive() {
        super.onInactive()
        complete()
    }

    private fun complete(){
        connectivity.unregisterNetworkCallback(networkCallback)
    }
}