package com.example.pawsitive.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeJsonAdapter {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @ToJson
    fun toJson(datetime: LocalDateTime): String {
        return datetime.format(formatter)
    }

    @FromJson
    fun fromJson(json: String): LocalDateTime {
        return LocalDateTime.parse(json, formatter)
    }
}