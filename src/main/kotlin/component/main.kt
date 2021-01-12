package component

import base.ReactComponent
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.button.mFab
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.input.mOutlinedInput
import com.ccfraser.muirwik.components.list.mList
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.css.*
import model.Advice
import model.Category
import model.Lifehack
import model.Quote
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.RState
import react.dom.div
import react.router.dom.navLink
import react.setState
import repository.AdviceRepository
import repository.CategoryRepository
import repository.LifehackRepository
import repository.QuoteRepository
import style.MainStyles
import styled.css
import styled.styledDiv

external interface MainProps : RProps

data class MainState(
    var isLoading: Boolean = false,
    var query: String? = null,
    var categories: List<Category> = listOf(),
    var lifehacks: List<Lifehack> = listOf(),
    var advice: Advice? = null,
    var quote: Quote? = null,
) : RState

@JsExport
class MainComponent(props: MainProps) : ReactComponent<MainProps, MainState>(props) {
    private val lifehackRepository: LifehackRepository = LifehackRepository()
    private val categoryRepository: CategoryRepository = CategoryRepository()
    private val adviceRepository: AdviceRepository = AdviceRepository()
    private val quoteRepository: QuoteRepository = QuoteRepository()

    init {
        state = MainState()
    }

    override fun componentDidMount() {
        componentScope.launch {
            setState { isLoading = true }

            val lifehacksDeferred = async { lifehackRepository.getAll() }
            val categoriesDeferred = async { categoryRepository.getAll() }
            val adviceDeferred = async { adviceRepository.getRandom() }
            val quoteDeferred = async { quoteRepository.getRandom() }

            val lifehacks = lifehacksDeferred.await()
            val categories = categoriesDeferred.await()
            val advice = adviceDeferred.await()
            val quote = quoteDeferred.await()

            setState {
                this.isLoading = false
                this.lifehacks = lifehacks
                this.categories = categories
                this.advice = advice
                this.quote = quote
            }
        }
    }

    override fun RBuilder.render() {
        div {
            if (state.isLoading) {
                mCircularProgress {
                    css { +MainStyles.centeredElement }
                }
            }

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
                css {
                    +MainStyles.sideListDiv
                    marginLeft = LinearDimension("32px")
                }

                div {
                    mOutlinedInput {
                        attrs {
                            onChange = { event ->
                                componentScope.launch {
                                    setState {
                                        query = (event.targetValue as? String) ?: ""
                                    }
                                }
                            }
                        }
                    }

                    mButton(
                        caption = "Search",
                        onClick = ::onSearchButtonClick
                    ) {
                        css {
                            marginLeft = LinearDimension("16px")
                        }
                    }
                }

                mTypography(
                    text = "Categories",
                    variant = MTypographyVariant.h6
                ) {
                    css {
                        marginTop = LinearDimension("16px")
                    }
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

                state.advice?.let { advice ->
                    mTypography(
                        text = "Advice",
                        variant = MTypographyVariant.h6
                    ) {
                        css {
                            marginTop = LinearDimension("32px")
                        }
                    }

                    mTypography(
                        text = advice.content,
                        variant = MTypographyVariant.body1
                    ) {
                        css {
                            marginTop = LinearDimension("8px")
                        }
                    }
                }

                state.quote?.let { quote ->
                    mTypography(
                        text = "Quote",
                        variant = MTypographyVariant.h6
                    ) {
                        css {
                            marginTop = LinearDimension("32px")
                        }
                    }

                    mTypography(
                        text = quote.content,
                        variant = MTypographyVariant.body1
                    ) {
                        css {
                            marginTop = LinearDimension("8px")
                        }
                    }

                    mTypography(
                        text = quote.author,
                        variant = MTypographyVariant.caption
                    )
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
