package com.example.pawsitive.view

import android.os.Handler
import android.util.Log

class SendGeolocationTask() {
    private val handler = Handler()
    private var isRunning = false

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                Log.d("test", "smth")
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