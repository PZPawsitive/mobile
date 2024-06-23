package com.example.pawsitive.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LocalDateJsonAdapter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @ToJson
    fun toJson(date: LocalDate): String {
        return date.format(formatter)
    }

    @FromJson
    fun fromJson(json: String): LocalDate {
        return LocalDate.parse(json, formatter)
    }
}
