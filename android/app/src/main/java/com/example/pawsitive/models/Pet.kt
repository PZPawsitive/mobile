package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class Pet (
    val id: String,
    val name: String,
    val species: String,
    val breed: String,
    val birthdate: LocalDate,
    val userId: String?
)