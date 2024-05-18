package com.example.pawsitive.api

import com.example.pawsitive.util.LocalDateJsonAdapter
import com.example.pawsitive.util.UUIDJsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object ServiceConfiguration {
    private val moshi = Moshi.Builder()
        .add(UUIDJsonAdapter())
        .add(LocalDateJsonAdapter())
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .client(getHttpClient())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val userService: UserService = retrofit.create(UserService::class.java)

    val petService: PetService = retrofit.create(PetService::class.java)

    val walkService: WalkService = retrofit.create(WalkService::class.java)
}

private fun getHttpClient(): OkHttpClient {
    val okHttpBuilder = OkHttpClient.Builder()

    okHttpBuilder.addInterceptor { chain ->
        val requestWithUserAgent = chain.request().newBuilder()
            .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE2MDI1OTEzLCJleHAiOjE3MTYwMjk1MTN9.nmsB1r5gmH1FJVQEmL2PZTmmXbUI4hlYf6qG3sG0D1Q")
            .build()
        chain.proceed(requestWithUserAgent)
    }
    return okHttpBuilder.build()
}