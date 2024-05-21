package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
data class GeopointDTO(
    val id: UUID,
    val latitude: Double,
    val longitude: Double,
    val history: String
)