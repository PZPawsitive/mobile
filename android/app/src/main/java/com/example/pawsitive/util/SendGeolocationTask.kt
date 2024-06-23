package com.example.pawsitive.util

import android.os.Handler
import android.util.Log
import com.example.pawsitive.models.SimpleGeopoint
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class SendGeolocationTask(apiViewModel: ApiViewModel, historyId: String?) {
    private val handler = Handler()
    private var isRunning = false

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    fun setGeolocation(lat: Double, long: Double) {
        latitude = lat
        longitude = long
    }
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                runBlocking {
                    val call: Call<String> =
                        apiViewModel.walkService.postGeopoint(historyId!!, SimpleGeopoint(latitude, longitude, LocalDateTime.now()))
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
                    // when there is no internet connection - todo
//                            val call: Call<List<String>> = apiViewModel.walkService.postMultipleGeopoints(historyId!!,
//                                listOf(SimpleGeopoint(latitude,longitude), SimpleGeopoint(latitude, longitude), SimpleGeopoint(latitude, longitude)))
//                            call.enqueue(object : Callback<List<String>> {
//                                override fun onResponse(
//                                    p0: Call<List<String>>,
//                                    p1: Response<List<String>>
//                                ) {
//                                    Log.d("retrofit", p1.body().toString())
//                                }
//
//                                override fun onFailure(
//                                    p0: Call<List<String>>,
//                                    p1: Throwable
//                                ) {
//                                    Log.d("retrofit", p1.message.toString())
////                            Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
//                                }
//
//                            })
                }
            }
            handler.postDelayed(this, 180000)

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