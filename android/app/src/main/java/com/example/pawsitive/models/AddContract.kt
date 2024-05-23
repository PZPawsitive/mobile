package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class AddContract(
    val description: String,
    val reward: Double,
    val location: SimpleGeopoint,
    val pets: List<String?>,
    val dangerous: Boolean,
)