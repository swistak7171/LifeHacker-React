package style

import kotlinx.css.*
import styled.StyleSheet

object MainStyles : StyleSheet("MainStyles", isStatic = true) {
    val textContainer by css {
        padding(5.px)

        backgroundColor = rgb(8, 97, 22)
        color = rgb(56, 246, 137)
    }

    val textInput by css {
        margin(vertical = 5.px)

        fontSize = 32.px
    }

    val mainListDiv by css {
        float = Float.left
        width = LinearDimension("30%")
    }

    val sideListDiv by css {
        float = Float.left
    }
} 
