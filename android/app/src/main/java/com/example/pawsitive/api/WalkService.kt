package com.example.pawsitive.api

import com.example.pawsitive.models.AddContract
import com.example.pawsitive.models.Contract
import com.example.pawsitive.models.GeopointDTO
import com.example.pawsitive.models.SimpleGeopoint
import org.osmdroid.util.GeoPoint
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface WalkService {

    @GET("api/contracts")
    fun getContracts(): Call<List<Contract>>

    @GET("api/contracts/{id}")
    fun getContract(@Path("id") id: String): Call<Contract>

    @GET("api/contracts/{id}/geopoints")
    fun getGeopoints(
        @Path("id") id: String
    ): Call<List<GeopointDTO>>

    @POST("api/geopoints/{contractId}")
    fun postGeopoint(
        @Path("contractId") contractId: String,
        @Body createGeopointDTO: SimpleGeopoint
    ): Call<String>

    @POST("api/geopoints/all/{contractId}")
    fun postMultipleGeopoints(
        @Path("contractId") contractId: String,
        @Body geopointsDTOs: List<SimpleGeopoint>
    ): Call<List<String>>

    @PUT("api/contracts/{id}/activate")
    fun acceptContract(@Path("id") id: String): Call<String>

    @PUT("api/contracts/{id}/complete")
    fun completeContract(@Path("id") id: String): Call<String>

    @POST("api/contracts")
    fun addContract(
        @Body createContractDTO: AddContract
    ): Call<String>

    @PUT("api/contracts/{id}/description")
    fun changeDescription(
        @Path("id") id: String,
        @Body description: String
        ) : Call<String>

    @DELETE("api/contracts/{id}")
    fun deleteContract(
        @Path("id") id: String
    ) : Call<String>
}