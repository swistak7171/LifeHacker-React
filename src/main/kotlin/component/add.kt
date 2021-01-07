package component

import base.ReactComponent
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import model.Category
import model.LifehackRequestBody
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.RState
import react.dom.*
import react.router.dom.redirect
import react.setState
import repository.CategoryRepository
import repository.LifehackRepository

external interface AddProps : RProps

data class AddState(
    var categories: List<Category> = listOf(),
    var content: String = "",
    var categoryId: Long? = null,
    var lifehackAdded: Boolean = false,
) : RState

@JsExport
class AddComponent(props: AddProps) : ReactComponent<AddProps, AddState>(props) {
    private val lifehackRepository: LifehackRepository = LifehackRepository()
    private val categoryRepository: CategoryRepository = CategoryRepository()

    init {
        state = AddState()
    }

    override fun componentDidMount() {
        componentScope.launch {
            val categories = categoryRepository.getAll()

            setState {
                this.categories = categories
                categoryId = categories.firstOrNull()?.id
            }
        }
    }

    override fun RBuilder.render() {
        if (state.lifehackAdded) {
            redirect(
                to = "/",
                push = true
            )
            return
        }

        form {
            attrs {
                onSubmitFunction = ::onSubmit
            }

            div {
                span {
                    +"Content"
                }

                input {
                    attrs {
                        type = InputType.text
                        onChangeFunction = { event ->
                            setState {
                                content = (event.target as HTMLInputElement).value
                            }
                        }
                    }
                }
            }

            div {
                span {
                    +"Category"
                }

                select {
                    label { +"Category" }
                    attrs {
                        onChangeFunction = { event ->
                            setState {
                                categoryId = (event.target as HTMLSelectElement).value.toLongOrNull()
                            }
                        }
                    }

                    state.categories.forEach { category ->
                        option {
                            +category.name

                            attrs {
                                value = category.id.toString()
                            }
                        }
                    }
                }
            }

            div {
                button {
                    +"Add"

                    attrs {
                        type = ButtonType.submit
                    }
                }
            }
        }
    }

    private fun onSubmit(event: Event) {
        event.preventDefault()
        val requestBody = LifehackRequestBody(
            content = state.content,
            categoryId = state.categoryId ?: return
        )

        componentScope.launch {
            val added = lifehackRepository.add(requestBody)

            setState {
                lifehackAdded = added
            }

            window.location.reload()
        }
    }
}