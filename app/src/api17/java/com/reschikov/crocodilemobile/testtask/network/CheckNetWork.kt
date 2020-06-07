package com.reschikov.crocodilemobile.testtask.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import com.reschikov.crocodilemobile.testtask.R

class CheckNetWork(private val context: Context ) : LiveData<String?>() {

    private val connectivity : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val strNoNetWork : String = context.getString(R.string.err_no_network)
    private val receiver = Receiver()

    override fun onActive() {
        super.onActive()
        startOff()
    }

    private fun startOff(){
        checkNet()
        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onInactive() {
        super.onInactive()
        complete()
    }

    private fun complete(){
        context.unregisterReceiver(receiver)
    }

    private fun checkNet(){
        val networkInfo = connectivity.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnectedOrConnecting) postValue(null)
        else postValue(strNoNetWork)
    }

    inner class Receiver : BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == ConnectivityManager.CONNECTIVITY_ACTION){
                    checkNet()
                }
            }
        }
    }
}