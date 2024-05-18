package com.example.pawsitive.api

import com.example.pawsitive.models.AddPetRequest
import com.example.pawsitive.models.LoginRequest
import com.example.pawsitive.models.User
import retrofit2.Call

class NetworkRepository(
    private val userService: UserService,
    private val petService: PetService,
    private val walkService: WalkService
) {
    fun login(request: LoginRequest): Call<User> {
        return userService.login(request)
    }
    fun addPet(request: AddPetRequest): Call<String> {
        return petService.addPet(request)
    }
}