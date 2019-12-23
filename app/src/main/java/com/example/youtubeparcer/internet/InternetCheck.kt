package com.example.youtubeparcer.internet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi

class InternetCheck : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent) {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activiNetwork: NetworkInfo? = cm?.activeNetworkInfo
        val isConnected: Boolean = activiNetwork?.isConnected == true

    }


}