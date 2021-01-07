import component.AddComponent
import component.MainComponent
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.js.onClickFunction
import react.RProps
import react.dom.h1
import react.dom.nav
import react.dom.render
import react.router.dom.hashRouter
import react.router.dom.navLink
import react.router.dom.route
import react.router.dom.switch

fun main() {
    window.onload = {
        val rootDiv = document.getElementById("root")
        render(rootDiv) {
            hashRouter {
                nav {
                    navLink<RProps>(to = "/") {
                        h1 {
                            +"LifeHacker"
                            attrs {
                                onClickFunction = { event ->
                                    val isMainPage = window.location.href.endsWith("#/")
                                    if (isMainPage) {
                                        window.location.reload()
                                    }
                                }
                            }
                        }
                    }
                }

                switch {
                    route(
                        path = "/",
                        exact = true
                    ) {
                        child(MainComponent::class) {}
                    }

                    route(
                        path = "/add",
                        exact = true
                    ) {
                        child(AddComponent::class) {}
                    }
                }
            }
        }
    }
}
