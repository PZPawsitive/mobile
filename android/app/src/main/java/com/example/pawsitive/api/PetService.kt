package com.example.pawsitive.api

import com.example.pawsitive.models.AddPetRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PetService {

    @POST("api/pets")
    fun addPet(@Body pet: AddPetRequest): Call<String>
}