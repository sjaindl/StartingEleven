import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework.BitcodeEmbeddingMode.BITCODE

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.google.playServices)
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

    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "S11 iOS dependencies"
        homepage = "https://starting-eleven-2019.firebaseapp.com/home"
        version = "1.0"
        ios.deploymentTarget = "15.5"

        podfile = project.file("../iosApp/Podfile")
        name = "composeApp"

        framework {
            baseName = "composeApp"
            isStatic = true

            embedBitcode(BITCODE)

            // TODO: Can pod dependencies from other libs be directly included without re-specification here?
            dependencies {
                implementation(project(":auth"))
            }
        }

        // xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        // xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

        pod(name = "GoogleSignIn")

        pod(name = "FirebaseCore") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            version = "11.6.0"
        }

        pod(name = "FirebaseAuth") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            version = "11.6.0"
        }

        pod(name = "FirebaseStorage") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            version = "11.6.0"
            // needed because of error:
            // Caused by: java.lang.IllegalStateException: Executing of 'xcodebuild -project Pods.xcodeproj -scheme FirebaseStorage -sdk iphoneos -configuration Release' failed with code 65 and message:
        }

        pod(name = "FirebaseFirestore") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            version = "11.6.0"
        }

        pod(name = "FBSDKCoreKit") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            version = "16.3.1"
        }
        pod(name = "FBSDKLoginKit") {
            extraOpts += listOf("-compiler-option", "-fmodules")
            version = "16.3.1"
            // TODO: Support Facebook Limited Sign-In with >= 17.4.0, as soon as the following issues are resolved:
            // https://github.com/firebase/firebase-ios-sdk/issues/8048
            // https://github.com/facebook/facebook-ios-sdk/issues/2455
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

            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)

            implementation(project(path = ":core"))
            implementation(project(path = ":auth"))
            implementation(project(path = ":home"))
            implementation(project(path = ":players"))
            implementation(project(path = ":profile"))
            implementation(project(path = ":standings"))
            implementation(project(path = ":team"))
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotest.assertions.core)
            implementation(libs.koin.test)
        }

        /*
        val wasmJsMain by getting {
        }
         */
    }
}

android {
    namespace = "com.sjaindl.s11"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile(srcPath = "src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/composeResources")

    defaultConfig {
        applicationId = "com.sjaindl.s11"

        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        val facebookClientToken = gradleLocalProperties(rootDir, providers).getProperty("facebookClientToken")
        manifestPlaceholders["facebookClientToken"] = facebookClientToken
    }

    signingConfigs {
        create("release") {
            keyAlias = gradleLocalProperties(rootDir, providers).getProperty("release.keyAlias")
            keyPassword = gradleLocalProperties(rootDir, providers).getProperty("release.password")
            storeFile = file(
                gradleLocalProperties(rootDir, providers).getProperty("release.storeFile")
            )
            storePassword = gradleLocalProperties(rootDir, providers).getProperty("release.password")
        }
    }


    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
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
