package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String
)