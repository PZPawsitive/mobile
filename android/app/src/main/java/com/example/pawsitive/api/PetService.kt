package com.example.pawsitive.api

import com.example.pawsitive.models.AddPetRequest
import com.example.pawsitive.models.Contract
import com.example.pawsitive.models.History
import com.example.pawsitive.models.Pet
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface PetService {

    @POST("api/pets")
    fun addPet(
        @Query("name") name: String,
        @Query("species") species: String,
        @Query("breed") breed: String,
        @Query("birthdate") birthdate: LocalDate,
        @Query("owner") owner: String,

        ): Call<String>

    @GET("api/users/{id}/pets")
    fun getPetsByUserId(@Path("id") id: String): Call<List<Pet>>

    @GET("api/pets/{id}")
    fun getPetById(@Path("id") id: String): Call<Pet>

    @GET("api/pets/{id}/history")
    fun getAllPetWalkHistoryByPetId(@Path("id") id: String): Call<List<Contract>>

}