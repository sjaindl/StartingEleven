import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.playServices)
}

android {
    namespace = "com.sjaindl.s11.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.sjaindl.s11"

        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 15
        versionName = "1.15"

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
            isDebuggable = false
            isShrinkResources = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(jdkVersion = 17)
}

dependencies {
    implementation(project(":composeApp"))
    implementation(project(":core"))
    implementation(project(":auth"))
    implementation(project(":players"))
    implementation(project(":standings"))
    implementation(project(":team"))

    implementation(compose.preview)
    debugImplementation(compose.uiTooling)

    implementation(libs.androidx.activity.compose)
    implementation(libs.play.services.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)

    implementation(libs.androidx.credentials)
    implementation(libs.credentials.play.services)
    implementation(libs.googleid)
    implementation(libs.facebook.login)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.core)

    implementation(libs.viewmodel.compose)
    implementation(libs.logging.napier)

    implementation(libs.firebase.common)
    implementation(libs.firebase.auth)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test.junit)
}



