package util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> encodeJson(value: T): String {
    return Json {
        ignoreUnknownKeys = true
    }.encodeToString(value)
}

inline fun <reified T> decodeJson(json: String): T {
    return Json {
        ignoreUnknownKeys = true
    }.decodeFromString(json)
}