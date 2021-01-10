package component

import base.ReactComponent
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.mFormControl
import com.ccfraser.muirwik.components.form.mFormLabel
import com.ccfraser.muirwik.components.input.mOutlinedInput
import com.ccfraser.muirwik.components.mSelect
import com.ccfraser.muirwik.components.menu.mMenuItem
import com.ccfraser.muirwik.components.targetValue
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.classes
import kotlinx.html.id
import kotlinx.html.js.onSubmitFunction
import model.Category
import model.LifehackRequestBody
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.form
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
                id = "add_form"
                onSubmitFunction = ::onSubmit
            }

            div {
                mFormControl {
                    mFormLabel("Content")
                    mOutlinedInput {
                        attrs {
                            onChange = { event ->
                                setState {
                                    console.log("Content: ${event.targetValue}")
                                    content = (event.targetValue as? String) ?: ""
                                }
                            }
                        }
                    }
                }
            }

            div {
                mFormControl {
                    mFormLabel("Category")
                    mSelect(null) {
                        attrs {
                            onChange = { event, element ->
                                setState {
                                    console.log("Category: ${event.targetValue}")
                                    categoryId = (event.targetValue as? String)?.toLongOrNull()
                                }
                            }
                        }

                        state.categories.forEach { category ->
                            mMenuItem {
                                +category.name

                                attrs {
                                    value = category.id.toString()
                                }
                            }
                        }
                    }
                }
            }

            div {
                mFormControl {
                    button {
                        +"Add"
                        attrs {
                            classes = setOf("MuiButtonBase-root MuiButton-root MuiButton-contained")
                            type = ButtonType.submit
                        }
                    }
                    
                    mButton("") {
                        attrs {
                            hidden = true
                        }
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
        console.log(requestBody.toString())

        componentScope.launch {
            val added = lifehackRepository.add(requestBody)

            setState {
                lifehackAdded = added
            }

            window.location.reload()
        }
    }
}