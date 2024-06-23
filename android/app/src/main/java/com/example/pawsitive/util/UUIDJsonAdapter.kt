package com.example.pawsitive.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.UUID

class UUIDJsonAdapter {
    @ToJson
    fun toJson(uuid: UUID): String {
        return uuid.toString()
    }

    @FromJson
    fun fromJson(json: String): UUID {
        return UUID.fromString(json)
    }
}