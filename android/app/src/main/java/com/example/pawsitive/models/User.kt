package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.util.UUID

@JsonClass(generateAdapter = true)
data class User(
    val token: String?,
    val id: UUID,
    val email: String,
    val birthdate: LocalDate,
    val firstName: String,
    val lastName: String,
    val phone: Int,
    val dogWalker: Boolean?,
    val location: String,
    val role: String?,
    val description: String,
    val profilePic: String,
    val pets: List<String>? // change
)