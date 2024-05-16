package com.example.pawsitive.api

import com.example.pawsitive.util.LocalDateJsonAdapter
import com.example.pawsitive.util.UUIDJsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object ServiceConfiguration {
    private val moshi = Moshi.Builder()
        .add(UUIDJsonAdapter())
        .add(LocalDateJsonAdapter())
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: Service = retrofit.create(Service::class.java)

}