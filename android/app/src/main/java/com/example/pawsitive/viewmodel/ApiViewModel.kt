package com.example.pawsitive.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.pawsitive.api.AdminService
import com.example.pawsitive.api.PetService
import com.example.pawsitive.api.RetrofitClient
import com.example.pawsitive.api.UserService
import com.example.pawsitive.api.WalkService

class ApiViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofitClient = RetrofitClient(application)

    val userService: UserService = retrofitClient.retrofit.create(UserService::class.java)

    val petService: PetService = retrofitClient.retrofit.create(PetService::class.java)

    val walkService: WalkService = retrofitClient.retrofit.create(WalkService::class.java)

    val adminService: AdminService = retrofitClient.retrofit.create(AdminService::class.java)
}