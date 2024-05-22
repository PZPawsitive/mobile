package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class IdWrapper(val id: String)

@JsonClass(generateAdapter = true)
data class AddPetRequest(
    val name: String,
    val species: String,
    val breed: String,
    val birthdate: LocalDate,
    val owner: String
)