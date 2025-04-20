import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(jdkVersion = 17)

    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.play.services.auth)
            implementation(project.dependencies.platform(libs.firebase.bom))

            implementation(libs.koin.android)
        }

        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.viewmodel.compose)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.material.icons.extended)
                implementation(libs.logging.napier)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.annotations)
               // implementation(libs.koin.viewmodel)

                implementation(libs.firebase.common)
                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)
                implementation(libs.firebase.storage)

                implementation(libs.coil.compose.core)
                implementation(libs.coil.compose)
                implementation(libs.coil.mp)
                implementation(libs.coil.network.ktor)

                implementation(project(":core"))
                implementation(project(":players"))
            }
        }

        iosMain.dependencies {
        }

        commonTest.dependencies {
            implementation(libs.kotest.assertions.core)
            implementation(libs.koin.test)
        }

        //val wasmJsMain by getting {
        //}
    }
}

android {
    namespace = "com.sjaindl.s11.team"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    dependencies {
        debugImplementation(compose.uiTooling)
        testImplementation(libs.junit)
        testImplementation(libs.kotlin.test.junit)
    }
}
