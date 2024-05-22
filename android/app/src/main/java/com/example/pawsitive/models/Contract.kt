package com.example.pawsitive.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Contract(
    val id: String,
    val dogWalker: String?,
    val description: String,
//    val user: String,
    val reward: Double,
    val pets: List<String>,
    val dangerous: Boolean,
    val active: Boolean,
    val completed:Boolean
)
