# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**StartingEleven** is a Kotlin Multiplatform (KMP) football manager app targeting Android and iOS, built with Compose Multiplatform. It integrates Firebase (Firestore, Auth, Storage, Remote Config, Messaging), Koin DI, Ktor networking, and SQLDelight.

## Build Commands

```bash
# Full build
./gradlew build

# Android
./gradlew :androidapp:assembleDebug
./gradlew :androidapp:installDebug
./gradlew :androidapp:bundleRelease

# iOS (CocoaPods)
./gradlew podInstall

# Tests
./gradlew test                           # All unit tests
./gradlew :composeApp:testAndroidHostTest  # Android debug unit tests
./gradlew iosSimulatorArm64Test          # iOS simulator tests
./gradlew connectedAndroidTest           # Instrumented tests (requires device)

# Lint
./gradlew lint
```

## Module Architecture

Feature-driven multiplatform modules. Each feature module depends on `:core`.

```
androidapp        – Android application entry point (MainActivity, S11Application, signing, manifest)
composeApp        – KMP library: navigation host, Koin init, shared app UI, iOS entry point
auth              – Firebase Auth + social sign-in (Google, Facebook, Apple via C-interop)
core              – Shared UI components, theme, Firebase repositories, DI base, nav routes
home              – Home screen, news, stats (Firebase Remote Config/Storage)
players           – Player list and detail
team              – Team formation, lineup recommender, betting (BetViewModel)
standings         – League standings display
profile           – User profile screen
photopicker       – Platform photo selection with EXIF support
iosApp            – SwiftUI entry point wrapping the composeApp KMM framework
```

### Source Set Layout (per module)

Each KMP module follows:
- `commonMain/` – shared Kotlin/Compose logic
- `androidMain/` – Android-specific implementations
- `iosMain/` – iOS-specific implementations
- `commonTest/`, `androidHostTest/`, `iosTest/` – test sources

### Dependency Injection

Koin 4.x with KSP annotations. Each module exposes a Koin module (e.g. `authModule`, `coreModule`). Root DI is assembled in `composeApp/AppModule.kt` and started in `S11Application` (in `androidapp`) / `MainViewController` (iOS).

### Navigation

`androidx.navigation` Compose with type-safe routes. Each feature module declares its own `NavGraph` extension function. `S11NavHost` in `composeApp` composes them all.

### ViewModels

Koin `viewModel {}` scoped. ViewModels use `kotlinx.coroutines` with `viewModelScope`. Firebase access goes through repository classes in `:core` or the feature module.

### Firebase Access Pattern

GitLive Firebase KMP wrappers (`dev.gitlive.firebase`). Repositories are in `core/src/commonMain/.../repository/` and feature `*ViewModel` files call them directly via Koin injection.

## Key Dependencies

| Library | Version | Purpose |
|---|---|---|
| Compose Multiplatform | 1.10.3 | UI framework |
| Kotlin | 2.3.20 | Language |
| Koin | 4.2.0 | Dependency injection |
| Ktor | 3.4.2 | HTTP client |
| GitLive Firebase | 2.4.0 | Firestore, Auth, Storage, Config, Messaging |
| SQLDelight | 2.3.2 | Multiplatform SQL |
| Coil | 3.4.0 | Image loading |
| Napier | 2.7.1 | Multiplatform logging |
| Kotest | 6.1.11 | Test assertions |

All versions are centralized in `gradle/libs.versions.toml`.

## Local Configuration

A `local.properties` file (not committed) is required for:
- Android release signing: `release.keyAlias`, `release.password`, `release.storeFile`
- `FLOWISE_API_KEY` – injected via BuildConfig for the AI assistant feature
- `FACEBOOK_CLIENT_TOKEN` – Facebook Login SDK
- Google Server Client ID

## Platforms & Targets

- **Android:** minSdk 28, targetSdk/compileSdk 36, versionCode 15
- **iOS:** Deployment target iOS 23.0, targets iosArm64 + iosSimulatorArm64 + iosX64
