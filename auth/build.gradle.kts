import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.swiftklib)
    alias(libs.plugins.buildkonfig)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

buildConfig {
    packageName = "com.sjaindl.s11.auth"
    val googleServerClientId = localProperties.getProperty("googleServerClientId", "")
    buildConfigField("String", "googleServerClientId", googleServerClientId)
}

kotlin {
    jvmToolchain(jdkVersion = 17)

    android {
        namespace = "com.sjaindl.s11.auth"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources {
            enable = true
        }

        withHostTest {}
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.compilations.getByName("main") {
            cinterops {
                create("applesignin")
                create("crypto")
            }
        }
    }

    cocoapods {
        summary = "S11 iOS auth dependencies"
        homepage = "https://starting-eleven-2019.firebaseapp.com/home"
        version = "1.0"
        ios.deploymentTarget = "23.0"

        podfile = project.file("../iosApp/Podfile")

        name = "auth"

        framework {
            baseName = "auth"
            isStatic = true
        }

        pod(name = "GoogleSignIn") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod(name = "FBSDKCoreKit") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }

        pod(name = "FBSDKLoginKit") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.play.services.auth)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.androidx.credentials)
            implementation(libs.credentials.play.services)
            implementation(libs.googleid)
            implementation(libs.facebook.login)

            implementation(libs.koin.android)
        }

        commonMain.dependencies {
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

            implementation(libs.firebase.common)
            implementation(libs.firebase.auth)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.annotations)
            implementation(libs.koin.viewmodel)

            implementation(project(":core"))
        }

        iosMain.dependencies {
        }

        commonTest.dependencies {
            implementation(libs.kotest.assertions.core)
            implementation(libs.koin.test)
        }

        getByName("androidHostTest").dependencies {
            implementation(libs.junit)
        }
    }
}

swiftklib {
    create("applesignin") {
        path = file("../iosApp/iosApp/applesignin")
        packageName("com.sjaindl.s11.applesignin")
    }

    create("crypto") {
        path = file("../iosApp/iosApp/crypto")
        packageName("com.sjaindl.s11.crypto")
    }
}
