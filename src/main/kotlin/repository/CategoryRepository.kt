package repository

import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.withContext
import model.Category
import util.decodeJson

class CategoryRepository {
    private val url: String = "http://localhost:8080/api/categories"

    suspend fun getAll(): List<Category> {
        return withContext(Dispatchers.Default) {
            window.fetch(url).await()
                .text().await()
                .let { decodeJson<List<Category>>(it) }
        }
    }
}