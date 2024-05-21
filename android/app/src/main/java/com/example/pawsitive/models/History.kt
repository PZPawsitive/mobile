package com.example.pawsitive.models

import com.squareup.moshi.JsonClass
import org.osmdroid.util.GeoPoint
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class History(
    val id: String,
    val description: String,
    val date: LocalDate,
    val geopoints: List<GeoPoint>,
    val pets: List<Pet>
)