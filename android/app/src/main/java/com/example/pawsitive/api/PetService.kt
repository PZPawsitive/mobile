package com.example.pawsitive.api

import com.example.pawsitive.models.AddPetRequest
import com.example.pawsitive.models.History
import com.example.pawsitive.models.Pet
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PetService {

    @POST("api/pets")
    fun addPet(@Body pet: AddPetRequest): Call<String>

    @GET("api/users/{id}/pets")
    fun getPetsByUserId(@Path("id") id: String): Call<List<Pet>>

    @GET("api/pets/{id}")
    fun getPetById(@Path("id") id: String): Call<Pet>

    @GET("api/pets/{id}/history")
    fun getAllPetWalkHistoryByPetId(@Path("id") id: String): Call<List<History>>

}