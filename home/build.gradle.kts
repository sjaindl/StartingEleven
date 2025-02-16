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

                implementation(libs.firebase.config)

                implementation(project(":core"))
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
    namespace = "com.sjaindl.s11.home"
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
