# 🛒 EcomApp - Android Product Catalogue MVP

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-green.svg)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/Hilt-2.60.1-orange.svg)](https://dagger.dev/hilt/)
[![KSP](https://img.shields.io/badge/KSP-2.3.14-purple.svg)](https://kotlinlang.org/docs/ksp-overview.html)
[![Chucker](https://img.shields.io/badge/Chucker-4.0.0-red.svg)](https://github.com/ChuckerTeam/chucker)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-brightgreen.svg)](https://developer.android.com/topic/architecture)

A production-grade, AI-assisted Android e-commerce product catalogue built with **Kotlin**, **Jetpack Compose (Material 3)**, **Hilt Dependency Injection (via KSP)**, and **Chucker Network Inspector**. Designed as a clean, modular foundation that meets all take-home code challenge requirements while adhering to modern Android engineering best practices.

---

## 🎨 Creative Home Page & UI Architecture

The Home Screen has been upgraded with a modern e-commerce storefront layout:
- **Branded App Bar**: Clean brand header with subtitle, shopping icon, and quick search action.
- **Search Trigger Card**: Fast search field redirecting to full catalogue query search.
- **Promotional Hero Banner**: Vibrant gradient banner ("Up to 50% OFF on Trending Essentials") with CTA button.
- **Category Pill Selector**: Interactive filter chips with category icons.
- **🔥 Top Rated Picks Carousel**: Horizontal scrolling product cards featuring floating star rating badges, review counts, and formatted prices.
- **🛍️ Recommended Catalogue Grid**: 2-column grid of curated product cards.

---

## 🛰️ Network Inspection with Chucker

For easy network debugging during evaluation, **Chucker Interceptor (4.0.0)** is wired into OkHttpClient in `NetworkModule`:
- Every HTTP request and response from [Fake Store API](https://fakestoreapi.com/) is recorded in real time.
- Swipe down the notification drawer while running the app in debug mode to tap the **Chucker notification** and inspect headers, payloads, status codes, and latency!

---

## 🏗️ Architecture & Component Layout

The application strictly adheres to **Clean Architecture**, **MVVM**, and the **Repository Pattern**:

```
                       ┌──────────────────────────────────────────┐
                       │            Presentation Layer            │
                       │   (Compose UI, Material 3, ViewModels)   │
                       └────────────────────┬─────────────────────┘
                                            │
                                            ▼
                       ┌──────────────────────────────────────────┐
                       │               Domain Layer               │
                       │   (Pure Models, Repository Interface)    │
                       └────────────────────▲─────────────────────┘
                                            │
                                            ▼
                       ┌──────────────────────────────────────────┐
                       │                Data Layer                │
                       │  (Retrofit + Chucker, DTOs, Repo Impl)   │
                       └──────────────────────────────────────────┘
```

---

## 🧪 Testing Strategy

Unit tests focus on verifying ViewModel reactive state pipelines and asynchronous repository interaction:

- **`HomeViewModelTest`**: Validates initial home data fetching and top 5 featured ranking calculation.
- **`SearchViewModelTest`**: Verifies query filtering logic across product titles and descriptions.
- **`CategoryViewModelTest`**: Tests category filtering and sorting logic.
- **`ProductDetailViewModelTest`**: Verifies product loading and enquiry form submission flow.

### Running Unit Tests
```bash
./gradlew app:testDebugUnitTest
```

---

## 🚀 Setup & Execution Guide

1. **Open Project**: Launch **Android Studio** (Koala / Ladybug / 2026.1+) and open `EcomApp`.
2. **Sync Dependencies**: Gradle will fetch Hilt 2.60.1, KSP 2.3.14, Retrofit, Chucker 4.0.0, Coil, and Compose Material 3 automatically.
3. **Launch Emulator**: Start an AVD running **API 24+** (API 34/35 recommended).
4. **Run App**: Click **Run (`▶`)** in Android Studio toolbar.
