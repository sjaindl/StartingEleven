rootProject.name = "StartingEleven"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }

        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven( "https://androidx.dev/storage/compose-compiler/repository")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
                maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
                maven( "https://androidx.dev/storage/compose-compiler/repository")
                maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
            }
        }
    }
}

include(":composeApp")
include(":auth")
include(":core")
include(":home")
include(":photopicker")
include(":players")
include(":profile")
include(":standings")
include(":team")
