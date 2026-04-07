plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(jdkVersion = 17)

    android {
        namespace = "com.sjaindl.s11.team"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources {
            enable = true
        }

        withHostTest {}
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.bundles.compose.android.debug)
            implementation(libs.androidx.activity.compose)

            implementation(libs.play.services.auth)
            implementation(project.dependencies.platform(libs.firebase.bom))

            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(libs.bundles.compose.common)
            implementation(libs.ui.tooling.preview)
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

        iosMain.dependencies {
        }

        commonTest.dependencies {
            implementation(libs.kotest.assertions.core)
            implementation(libs.koin.test)
        }

        getByName("androidHostTest").dependencies {
            implementation(libs.junit)
            implementation(libs.kotlin.test.junit)
        }
    }
}
