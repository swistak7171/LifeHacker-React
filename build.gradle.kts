plugins {
    val kotlinVersion = "1.4.21"
    kotlin("js") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

group = "pl.kamilszustak"
version = "0.0.1"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx/") }
}

dependencies {
    testImplementation(kotlin("test-js"))
    implementation("org.jetbrains:kotlin-react:17.0.0-pre.132-kotlin-1.4.21")
    implementation("org.jetbrains:kotlin-react-dom:17.0.0-pre.132-kotlin-1.4.21")
    implementation("org.jetbrains:kotlin-styled:5.2.0-pre.133-kotlin-1.4.21")
    implementation("org.jetbrains:kotlin-react-router-dom:5.2.0-pre.132-kotlin-1.4.21")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-io:0.1.16")

    implementation(npm("@material-ui/core", "4.11.2"))
    implementation(npm("react-star-rating-component", "1.4.1"))
}

kotlin {
    js(LEGACY) {
        browser {
            binaries.executable()
            webpackTask {
                cssSupport.enabled = true
            }
            runTask {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }
}

tasks.getByName("build") {
    val hasUrl = project.hasProperty("url")
    if (!hasUrl) {
        return@getByName
    }

    val url = project.property("url")
    file("build\\distributions\\.config").run {
        createNewFile()
        writeText(url.toString())
    }

    doLast {
        val indexFile = file("build\\distributions\\index.html")
        val htmlContent = indexFile.readText()
        val parts = htmlContent.split("<script")
        if (parts.size != 2) {
            return@doLast
        }

        val newParts = listOf(
            parts[0],
            "<input id=\"lifehacker_url\" type=\"hidden\" value=\"$url\">",
            "\n<script",
            parts[1]
        )

        val newHtmlContent = newParts.joinToString("")
        indexFile.writeText(newHtmlContent)
    }
}