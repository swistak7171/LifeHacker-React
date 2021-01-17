package repository

import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.withContext
import model.Lifehack
import model.LifehackRequestBody
import org.w3c.fetch.RequestInit
import util.decodeJson
import util.encodeJson

class LifehackRepository : Repository("/api/lifehacks") {
    suspend fun getAll(
        query: String? = null,
        categoryId: Long? = null
    ): List<Lifehack> {
        return withContext(Dispatchers.Default) {
            val customUrl = buildString {
                append(url)
                append("?")
                if (query != null) {
                    append("query=")
                    append(query)
                }

                if (categoryId != null) {
                    append("&category-id=")
                    append(categoryId)
                }
            }

            window.fetch(customUrl).await()
                .text().await()
                .let { decodeJson<List<Lifehack>>(it) }
                .sortedByDescending(Lifehack::id)
        }
    }

    suspend fun add(requestBody: LifehackRequestBody): Boolean {
        val json = encodeJson(requestBody)

        return withContext(Dispatchers.Default) {
            window.fetch(url, RequestInit(
                method = "POST",
                body = json,
                headers = defaultHeaders
            )).await().ok
        }
    }

    suspend fun rate(lifehackId: Long, rating: Int): Boolean {
        return withContext(Dispatchers.Default) {
            window.fetch("$url/$lifehackId/rating", RequestInit(
                method = "POST",
                body = rating,
                headers = defaultHeaders
            )).await().ok
        }
    }
}