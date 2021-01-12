package component

import base.ReactComponent
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.mFab
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.list.mList
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Category
import model.Lifehack
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.input
import react.router.dom.navLink
import react.setState
import repository.CategoryRepository
import repository.LifehackRepository
import style.MainStyles
import styled.css
import styled.styledDiv

external interface MainProps : RProps

data class MainState(
    var query: String? = null,
    var categories: List<Category> = listOf(),
    var lifehacks: List<Lifehack> = listOf(),
) : RState

@JsExport
class MainComponent(props: MainProps) : ReactComponent<MainProps, MainState>(props) {
    private val lifehackRepository: LifehackRepository = LifehackRepository()
    private val categoryRepository: CategoryRepository = CategoryRepository()

    init {
        state = MainState()
    }

    override fun componentDidMount() {
        componentScope.launch {
            val lifehacksDeferred = async { lifehackRepository.getAll() }
            val categoriesDeferred = async { categoryRepository.getAll() }

            val lifehacks = lifehacksDeferred.await()
            val categories = categoriesDeferred.await()

            setState {
                this.lifehacks = lifehacks
                this.categories = categories
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                position = Position.relative
            }

            input {
                attrs {
                    type = InputType.text
                    onChangeFunction = { event ->
                        componentScope.launch {
                            setState {
                                query = (event.targetValue as? String) ?: ""
                            }
                        }
                    }
                }
            }

            button {
                +"Search"
                attrs {
                    onClickFunction = ::onSearchButtonClick
                }
            }
        }

        div {
            styledDiv {
                mTypography("Lifehacks") {
                    attrs {
                        variant = MTypographyVariant.h6
                    }
                }

                css {
                    +MainStyles.mainListDiv
                }

                mList {
                    state.lifehacks.forEach { lifehack ->
                        val category = state.categories.find { it.id == lifehack.categoryId }

                        mCard {
                            css {
                                marginTop = LinearDimension("8px")
                            }

                            mCardContent {
                                if (category != null) {
                                    mTypography(category.name) {
                                        attrs {
                                            variant = MTypographyVariant.caption
                                        }
                                    }
                                }

                                mTypography(lifehack.content) {
                                    attrs {
                                        variant = MTypographyVariant.body1
                                    }
                                }
                            }
                        }
                    }
                }
            }

            styledDiv {
                mTypography("Categories") {
                    attrs {
                        variant = MTypographyVariant.h6
                    }
                }

                css {
                    +MainStyles.sideListDiv
                    marginLeft = LinearDimension("32px")
                }

                state.categories.forEach { category ->
                    styledDiv {
                        css {
                            marginTop = LinearDimension("8px")
                        }

                        mChip(category.name) {
                            attrs {
                                id = category.id.toString()
                                onClick = { event ->
                                    onCategoryClick(category.id)
                                }
                            }
                        }
                    }
                }
            }
        }

        navLink<MainProps>(to = "/add") {
            mFab(
                size = MButtonSize.large
            ) {
                css {
                    position = Position.fixed
                    left = LinearDimension("32.5%")
                    bottom = LinearDimension("32px")
                }

                mTypography(
                    text = "+",
                    variant = MTypographyVariant.h6
                )
            }
        }

//        styledDiv {
//            css {
//                +MainStyles.textContainer
//            }
//            +"Hello, World!"
//        }
//
//        styledInput {
//            css {
//                +MainStyles.textInput
//            }
//            attrs {
//                type = InputType.text
//                onChangeFunction = { event ->
//                }
//            }
//        }
    }

    private fun onSearchButtonClick(event: Event) {
        componentScope.launch {
            val lifehacks = lifehackRepository.getAll(query = state.query)

            setState {
                this.lifehacks = lifehacks
            }
        }
    }

    private fun onCategoryClick(categoryId: Long) {
        componentScope.launch {
            val lifehacks = lifehackRepository.getAll(categoryId = categoryId)
            setState {
                this.lifehacks = lifehacks
            }
        }
    }
}
