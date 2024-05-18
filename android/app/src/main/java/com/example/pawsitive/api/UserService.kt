package com.example.pawsitive.api

import com.example.pawsitive.models.LoginRequest
import com.example.pawsitive.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("api/auth/authenticate")
    fun login(@Body request: LoginRequest): Call<User>
}