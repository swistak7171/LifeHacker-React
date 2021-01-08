package repository

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement

abstract class Repository(baseEndpoint: String) {
    protected val url: String

    init {
        val urlInput = document.getElementById("lifehacker_url") as? HTMLInputElement
        val url = urlInput?.value ?: "http://localhost:8080"
        requireNotNull(url) { "LifeHacker backend URL is not provided" }

        this.url = "$url$baseEndpoint"
    }
}