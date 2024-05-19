package com.example.pawsitive.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerificationRequest(val token: String, val email: String)