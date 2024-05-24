package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class Contract(
    val id: String,
    val dogWalker: String?,
    val owner: ContractOwner?,
    val description: String,
    val reward: Double,
    val pets: List<String>,
    val location: SimpleGeopoint,
    val createdAt: LocalDateTime,
    val acceptedAt: LocalDateTime?,
    val completedAt: LocalDateTime?,
    val dangerous: Boolean,
    val active: Boolean,
    val completed:Boolean
)
