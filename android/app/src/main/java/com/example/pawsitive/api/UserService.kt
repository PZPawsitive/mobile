package com.example.pawsitive.api

import com.example.pawsitive.models.LoginRequest
import com.example.pawsitive.models.RegisterRequest
import com.example.pawsitive.models.User
import com.example.pawsitive.models.VerificationRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("api/auth/authenticate")
    fun login(@Body request: LoginRequest): Call<User>

    @POST("api/auth/register")
    fun register(@Body request: RegisterRequest): Call<User>

    @GET("api/auth/verify")
    fun verify(
        @Query("token") token: String,
        @Query("email") email: String
    ): Call<Void>

    @POST("api/auth/resend")
    fun resend(email: String): Call<Void>
}