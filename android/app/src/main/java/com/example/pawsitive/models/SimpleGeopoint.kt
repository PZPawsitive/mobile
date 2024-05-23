package com.example.pawsitive.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SimpleGeopoint(
    val latitude: Double,
    val longitude: Double
)