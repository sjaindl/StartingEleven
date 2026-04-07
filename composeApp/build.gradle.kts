import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildkonfig)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

buildConfig {
    packageName = "com.sjaindl.s11"
    val flowiseApiKey = localProperties.getProperty("FLOWISE_API_KEY", "")
    buildConfigField("String", "FLOWISE_API_KEY", flowiseApiKey)
}

kotlin {

    /*
    js(IR) {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }
     */

    /*
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
     */

    jvmToolchain(jdkVersion = 17)

    android {
        namespace = "com.sjaindl.s11"
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

    cocoapods {
        summary = "S11 iOS dependencies"
        homepage = "https://starting-eleven-2019.firebaseapp.com/home"
        version = "1.0"
        ios.deploymentTarget = "23.0"

        podfile = project.file("../iosApp/Podfile")
        name = "composeApp"

        framework {
            baseName = "composeApp"
            isStatic = true
        }

        // xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        // xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

        pod(name = "FirebaseCore") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }

        pod(name = "FirebaseAuth") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }

        pod(name = "FirebaseStorage") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
            // needed because of error:
            // Caused by: java.lang.IllegalStateException: Executing of 'xcodebuild -project Pods.xcodeproj -scheme FirebaseStorage -sdk iphoneos -configuration Release' failed with code 65 and message:
        }

        pod(name = "FirebaseFirestore") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }

        pod(name = "FirebaseMessaging") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }

        pod(name = "FirebaseRemoteConfig") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }

        // TODO: Can pod dependencies from other libs be directly included without re-specification here?
        // https://youtrack.jetbrains.com/issue/KT-30841/Support-consuming-CocoaPod-dependency-in-MPP-library-project
        // https://youtrack.jetbrains.com/issue/KT-44704/KMM-library-needs-pod-in-iOS-app-Podfile
        // https://youtrack.jetbrains.com/issue/KT-41830/CocoaPods-integration-Support-link-only-mode-for-pods
        //export(project(":auth"))
        pod(name = "GoogleSignIn") {
            linkOnly = true
        }

        pod(name = "FBSDKCoreKit") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }

        pod(name = "FBSDKLoginKit") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            linkOnly = true
        }

        compilerOptions {
            // do not optimize out variables in coroutines
            freeCompilerArgs.add("-Xdebug")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(libs.androidx.activity.compose)

            implementation(libs.play.services.auth)
            implementation(project.dependencies.platform(libs.firebase.bom))

            implementation(libs.koin.android)
            implementation(libs.koin.compose)
            implementation(libs.ktor.client.android)
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
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.storage)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.annotations)
           // implementation(libs.koin.viewmodel)

            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)
            implementation(libs.mobile.rag.assistant)

            implementation(project(path = ":core"))
            implementation(project(path = ":auth"))
            implementation(project(path = ":home"))
            implementation(project(path = ":players"))
            implementation(project(path = ":profile"))
            implementation(project(path = ":standings"))
            implementation(project(path = ":team"))
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }

        commonTest.dependencies {
            implementation(libs.koin.test)
            implementation(libs.kotest.assertions.core)
        }

        getByName("androidHostTest").dependencies {
            implementation(libs.junit)
            implementation(libs.kotlin.test.junit)
        }

    }
}

