package component

import base.ReactComponent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Category
import model.Lifehack
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.RState
import react.dom.*
import react.router.dom.navLink
import react.setState
import repository.CategoryRepository
import repository.LifehackRepository

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
        div {
            input {
                attrs {
                    type = InputType.text
                    onChangeFunction = { event ->
                        componentScope.launch {
                            setState {
                                query = (event.target as HTMLInputElement).value
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

            navLink<MainProps>(to = "/add") {
                button {
                    +"Add"
                }
            }
        }

        div {
            h4 {
                +"Categories"
            }

            state.categories.forEach { category ->
                span {
                    +category.name
                    attrs {
                        id = category.id.toString()
                        onClickFunction = ::onCategoryClick
                    }
                }
            }
        }

        div {
            h4 {
                +"Lifehacks"
            }

            state.lifehacks.forEach { lifehack ->
                p {
                    +lifehack.content
                }
            }
        }


//        styledDiv {
//            css {
//                +WelcomeStyles.textContainer
//            }
//            +"Hello, World!"
//        }

//        styledInput {
//            css {
//                +WelcomeStyles.textInput
//            }
//            attrs {
//                type = InputType.text
//                onChangeFunction = { event ->
//                    setState(
//                        MainState(name = (event.target as HTMLInputElement).value)
//                    )
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

    private fun onCategoryClick(event: Event) {
        val id = (event.target as HTMLElement).id.toLongOrNull() ?: return
        componentScope.launch {
            val lifehacks = lifehackRepository.getAll(categoryId = id)
            setState {
                this.lifehacks = lifehacks
            }
        }
    }
}
