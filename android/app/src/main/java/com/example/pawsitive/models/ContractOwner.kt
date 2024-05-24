package com.example.pawsitive.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContractOwner(
    val id: String,
    val firstName: String,
    val lastName: String
)