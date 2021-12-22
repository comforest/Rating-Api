package com.nomean.rating.api.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component


object JsonUtil {

    private val objectMapper by lazy {
        ObjectMapper()
    }

    fun convertToJson(obj: Any) : String {
        return objectMapper.writeValueAsString(obj)
    }
}