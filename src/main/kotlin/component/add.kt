package component

import base.ReactComponent
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.form.mFormControl
import com.ccfraser.muirwik.components.form.mFormLabel
import com.ccfraser.muirwik.components.input.mOutlinedInput
import com.ccfraser.muirwik.components.mSelect
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.menu.mMenuItem
import com.ccfraser.muirwik.components.targetValue
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.css.LinearDimension
import kotlinx.css.marginTop
import kotlinx.css.width
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
import react.dom.div
import react.router.dom.redirect
import react.setState
import repository.CategoryRepository
import repository.LifehackRepository
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledForm

external interface AddProps : RProps

data class AddState(
    var categories: List<Category> = listOf(),
    var content: String = "",
    var categoryId: Long? = null,
    var lifehackAdded: Boolean = false,
) : RState

private const val FORM_WIDTH: String = "400px"

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

        styledDiv {
            css {
                marginTop = LinearDimension("16px")
            }

            mTypography(
                text = "Add lifehack",
                variant = MTypographyVariant.h6
            ) {
                css {
                    marginTop = LinearDimension("16px")
                }
            }

            styledForm {
                css {
                    marginTop = LinearDimension("8px")
                }

                attrs {
                    id = "add_form"
                    onSubmitFunction = ::onSubmit
                }

                div {
                    mFormControl {
                        css {
                            width = LinearDimension(FORM_WIDTH)
                        }

                        mFormLabel("Content")
                        mOutlinedInput(
                            placeholder = "Content",
                            multiline = true,
                            rows = 5,
                            onChange = { event ->
                                setState {
                                    content = (event.targetValue as? String) ?: ""
                                }
                            }
                        ) {
                            css {
                                marginTop = LinearDimension("4px")
                            }
                        }
                    }
                }

                styledDiv {
                    css {
                        marginTop = LinearDimension("24px")
                        width = LinearDimension(FORM_WIDTH)
                    }

                    mFormControl {
                        mFormLabel("Category")
                        mSelect(
                            value = null,
                            variant = MFormControlVariant.outlined,
                            onChange = { event, element ->
                                setState {
                                    categoryId = (event.targetValue as? String)?.toLongOrNull()
                                }
                            }
                        ) {
                            css {
                                marginTop = LinearDimension("4px")
                                width = LinearDimension(FORM_WIDTH)
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

                styledDiv {
                    css {
                        marginTop = LinearDimension("24px")
                    }

                    mFormControl {
                        styledButton {
                            +"Add"

                            css {
                                width = LinearDimension(FORM_WIDTH)
                            }

                            attrs {
                                classes = setOf("MuiButtonBase-root MuiButton-root MuiButton-contained")
                                type = ButtonType.submit
                            }
                        }

                        mButton("") {
                            css {
                                marginTop = LinearDimension("16px")
                            }

                            attrs {
                                hidden = true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onSubmit(event: Event) {
        event.preventDefault()
        val content = state.content.trim()
        if (content.isBlank()) {
            window.alert("Content not entered")
            return
        }

        val categoryId = state.categoryId ?: run {
            window.alert("Category not selected")
            return
        }

        val requestBody = LifehackRequestBody(
            content = content,
            categoryId = categoryId
        )

        componentScope.launch {
            val added = lifehackRepository.add(requestBody)
            if (!added) {
                window.alert("An error occurred while adding the lifehack")
                return@launch
            }

            setState {
                lifehackAdded = added
            }

            window.location.reload()
        }
    }
}