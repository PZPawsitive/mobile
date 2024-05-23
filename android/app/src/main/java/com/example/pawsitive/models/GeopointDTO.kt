package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.util.UUID

@JsonClass(generateAdapter = true)
data class GeopointDTO(
    val id: UUID,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime
)