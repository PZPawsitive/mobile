package com.example.pawsitive.api

import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.util.UUID
@JsonClass(generateAdapter = true)
data class LoginResponse(
    val token: String,
    val id: UUID,
    val email: String,
    val birthdate: LocalDate,
    val firstName: String,
    val lastName: String,
    val phone: Int,
    val description: String,
    val profilePic: String,
    val animals: List<Any>
)