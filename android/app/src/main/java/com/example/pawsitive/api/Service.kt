package com.example.pawsitive.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Service {

    @POST("api/auth/authenticate")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}