package com.example.pawsitive.api

import com.example.pawsitive.models.LoginRequest
import com.example.pawsitive.models.RegisterRequest
import com.example.pawsitive.models.SimpleGeopoint
import com.example.pawsitive.models.User
import com.example.pawsitive.models.VerificationRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface UserService {
    @POST("api/auth/authenticate")
    fun login(@Body request: LoginRequest): Call<User>

    @POST("api/auth/register")
    fun register(@Body request: RegisterRequest): Call<User>

    @POST("api/auth/verify")
    fun verify(
        @Query("token") token: String,
        @Query("email") email: String
    ): Call<String>

    @POST("api/auth/resend")
    fun resend(@Body email: String): Call<String>

    @GET("api/auth/checkSession")
    fun checkSession(@Query("token") token: String): Call<String>

    @GET("api/users/{id}")
    fun getUserById(@Path("id") id: String): Call<User>

    @POST("api/users/dogwalkers-nearby")
    fun getDogWalkersNearby(
        @Query("range") range: Double,
        @Body createGeopointDTO: SimpleGeopoint
    ): Call<List<User>>

    @GET("api/users/dogwalkers")
    fun getDogWalkers() : Call<List<User>>

    @PUT("api/users/{id}/dogwalker")
    fun updateDogwalker(@Path("id") id: String) : Call<String>

}