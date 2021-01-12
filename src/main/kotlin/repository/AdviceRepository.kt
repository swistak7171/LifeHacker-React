package repository

import kotlinx.browser.window
import kotlinx.coroutines.await
import model.Advice
import model.AdviceSlip
import util.decodeJson

class AdviceRepository {
    private val baseUrl: String = "https://api.adviceslip.com/advice"

    suspend fun getRandom(): Advice {
        return window.fetch(baseUrl).await()
            .text().await()
            .let { decodeJson<AdviceSlip>(it) }
            .advice
    }
}