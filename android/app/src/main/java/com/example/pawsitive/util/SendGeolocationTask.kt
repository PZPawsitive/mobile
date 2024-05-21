package com.example.pawsitive.util

import android.os.Handler
import android.util.Log
import com.example.pawsitive.models.GeopointDTO
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class SendGeolocationTask(apiViewModel: ApiViewModel, historyId: String?) {
    private val handler = Handler()
    private var isRunning = false

    private var latitude: Double = 0.0
    private var longtitude: Double = 0.0
//    private var historyId: String = ""

    fun setGeolocation(lat: Double, long: Double) {
        latitude = lat
        longtitude = long
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                Log.d("test", "$latitude $longtitude $historyId")
                runBlocking {
                    val call: Call<String> = apiViewModel.walkService.postGeopoint(GeopointDTO(UUID.randomUUID(),latitude, longtitude, "6f30e3f4-fa49-4516-a307-4b311bcb3b02"))
                    call.enqueue(object : Callback<String> {
                        override fun onResponse(
                            p0: Call<String>,
                            p1: Response<String>
                        ) {
                            Log.d("retrofit", p1.body().toString())
                        }

                        override fun onFailure(
                            p0: Call<String>,
                            p1: Throwable
                        ) {
                            Log.d("retrofit", p1.message.toString())
//                            Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
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