package com.example.pawsitive.api

import com.example.pawsitive.models.AddPetRequest
import com.example.pawsitive.models.Pet
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PetService {

    @POST("api/pets")
    fun addPet(@Body pet: AddPetRequest): Call<String>

    @GET("api/pets")
    fun getPetsByUserId()

    @GET("api/pet")
    fun getPetById(@Path("id") id: String): Call<Pet>
}