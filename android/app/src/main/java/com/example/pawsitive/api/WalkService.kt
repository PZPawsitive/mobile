package com.example.pawsitive.api

import com.example.pawsitive.models.Contract
import com.example.pawsitive.models.GeopointDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WalkService {

    @GET("api/contracts")
    fun getContracts(): Call<List<Contract>>

    @GET("api/contracts/{id}")
    fun getContract(@Path("id") id: String): Call<Contract>

    @POST("api/geopoints")
    fun postGeopoint(@Body geopoint: GeopointDTO): Call<String>

    @PUT("api/contracts/{id}/active")
    fun acceptContract(@Path("id") id:String): Call<String>

    @PUT("api/contracts/{id}/complete")
    fun completeContract(@Path("id") id:String): Call<String>

    @POST("api/contract")
    fun addContract()
}