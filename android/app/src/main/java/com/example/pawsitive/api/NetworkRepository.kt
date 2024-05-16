package com.example.pawsitive.api

import retrofit2.Call

class NetworkRepository(private val service: Service) {
    fun login(request: LoginRequest): Call<LoginResponse> {
        return service.login(request)
    }
}