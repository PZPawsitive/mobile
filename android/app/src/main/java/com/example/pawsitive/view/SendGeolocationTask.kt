package com.example.pawsitive.view

import android.os.Handler
import android.util.Log

class SendGeolocationTask() {
    private val handler = Handler()
    private var isRunning = false

    private var latitude: Double = 0.0
    private var longtitude: Double = 0.0

    fun setGeolocation(lat: Double, long: Double) {
        latitude = lat
        longtitude = long
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                Log.d("test", "$latitude $longtitude")
                handler.postDelayed(this, 15000)
            }
        }
    }

    fun start() {
        if (!isRunning) {
            isRunning = true
            handler.post(runnable)
        }
    }

    fun stop() {
        isRunning = false
        handler.removeCallbacks(runnable)
    }

}