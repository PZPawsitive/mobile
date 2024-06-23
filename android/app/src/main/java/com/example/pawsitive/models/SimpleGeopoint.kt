package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class SimpleGeopoint(
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime
)