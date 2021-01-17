package repository

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.fetch.Headers

abstract class Repository(baseEndpoint: String) {
    protected val url: String
    protected val defaultHeaders: Headers
        get() = Headers().apply {
            append("Content-Type", "application/json")
        }

    init {
        val urlInput = document.getElementById("lifehacker_url") as? HTMLInputElement
        val url = urlInput?.value ?: "http://localhost:8080"
        requireNotNull(url) { "LifeHacker backend URL is not provided" }

        this.url = "$url$baseEndpoint"
    }
}