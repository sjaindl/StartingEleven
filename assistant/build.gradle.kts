plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("ChatDatabase") {
            packageName.set("com.sjaindl.s11.assistant.database")
        }
    }
}

kotlin {
    jvmToolchain(jdkVersion = 17)

    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.koin.android)
            implementation(libs.koin.compose)
            implementation(libs.sqldelight.android.driver)
        }

        commonMain.dependencies {
            implementation(projects.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.annotations)
            implementation(libs.koin.viewmodel)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(libs.viewmodel.compose)
            implementation(libs.material.icons.extended)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)

        }

        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
            implementation(libs.sqldelight.native.driver)
        }

        commonTest.dependencies {
            implementation(libs.koin.test)
        }
    }
}

android {
    namespace = "com.sjaindl.s11.assistant"
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
