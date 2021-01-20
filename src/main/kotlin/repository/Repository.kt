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
        val value = urlInput?.value
        requireNotNull(value) { "LifeHacker backend URL is not provided" }

        val hasPrefix = value.startsWith("http://") || value.startsWith("https://")
        val url = if (hasPrefix) {
            value
        } else {
            "http://$value"
        }

        this.url = "$url$baseEndpoint"
    }
}