package repository

import kotlinx.browser.window
import kotlinx.coroutines.await
import model.Quote
import util.decodeJson

class QuoteRepository {
    private val baseUrl: String = "https://api.quotable.io"

    suspend fun getRandom(): Quote {
        return window.fetch("$baseUrl/random").await()
            .text().await()
            .let { decodeJson(it) }
    }
}