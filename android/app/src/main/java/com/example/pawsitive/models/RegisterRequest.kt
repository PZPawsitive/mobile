package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import org.osmdroid.util.GeoPoint
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val birthdate: LocalDate,
    val phone: Int,
    val description: String,
    val profilePic: String,
    val role: String,
    val location: SimpleGeopoint
)