# Architectural Decisions & Assumptions

This document outlines key technical decisions, assumptions, trade-offs, scalability strategies, and future roadmap items for EcomApp.

---

## 1. Dependency Injection with Hilt & KSP
- **Framework Selection**: Adopted **Hilt 2.60.1** with **Google KSP (2.3.14)** via Gradle Version Catalog (`libs.versions.toml`).
- **Single Responsibility Modules**:
  - `NetworkModule`: Manages Retrofit network instance creation, OkHttpClient, and Chucker interceptor.
  - `RepositoryModule`: Binds domain repository abstractions to concrete data implementations.
- **Feature-Scoped ViewModels**: Used `@HiltViewModel` and `@Inject constructor` for `HomeViewModel`, `CategoryViewModel`, `SearchViewModel`, and `ProductDetailViewModel`, eliminating boilerplate factories and ensuring loose coupling.

---

## 2. Assumptions Made
- **Product Metadata & Data Source**: Public [Fake Store API](https://fakestoreapi.com/) was utilized via Retrofit to supply realistic product catalogues, categories, and ratings.
- **Realtime Dynamic Ranking**: Curated top home page featured items by computing a weighted score (`rating.rate * rating.count`) on live API data to drive the **Realtime Hero Banner** and **Top Rated Picks Carousel**.
- **Enquiry Handling**: Simulated sales inbox email dispatch with an asynchronous repository delay and success response, presenting a clean confirmation modal to the customer.

---

## 3. Trade-Offs & Simplified Requirements
- **Client-Side Only Implementation**: Per challenge guidance allowing a client-only solution, we omitted a separate backend server container. Catalogue data flows through a robust repository layer with reactive state management.

---

## 4. Growing Beyond the PoC (Architectural Scalability)

### 4.1 Offline-First Caching Preparedness
The current architecture strictly obeys the **Single Source of Truth (SSOT)** pattern via `ProductRepository`. Because ViewModels only consume domain data through `StateFlow` streams exposed by `ProductRepository`, upgrading the app to an **offline-first data layer** requires zero changes to the UI or ViewModel code:
- **Room Database Integration**: A local Room SQLite cache can be inserted directly inside `ProductRepositoryImpl`.
- **Sync Strategy**: The repository will return cached local data instantly while fetching fresh data from the network in the background (`NetworkBoundResource` pattern).

### 4.2 Multi-Module Architecture for Rapid Feature Expansion
As app capabilities expand to include new e-commerce domains (e.g., *Checkout & Cart*, *User Profile*, *Order Tracking*, *Wishlist*, and *Reviews & Ratings*), the current feature-based package layout (`ui.home`, `ui.category`, `ui.search`, `ui.detail`) provides an effortless migration path to an enterprise **Multi-Module Gradle Architecture**:
```
                              ┌────────────────────────┐
                              │       :app (App)       │
                              └───────────┬────────────┘
                                          │
    ┌───────────────────────┬─────────────┴─────────────┬───────────────────────┐
    ▼                       ▼                           ▼                       ▼
┌───────────────────┐ ┌───────────────────┐   ┌───────────────────┐   ┌───────────────────┐
│   :feature:home   │ │  :feature:detail  │   │ :feature:checkout │   │  :feature:orders  │
└─────────┬─────────┘ └─────────┬─────────┘   └─────────┬─────────┘   └─────────┬─────────┘
          │                     │                       │                       │
          └─────────────────────┴───────────┬───────────┴───────────────────────┘
                                            │
                  ┌─────────────────────────┴─────────────────────────┐
                  ▼                                                   ▼
        ┌───────────────────┐                               ┌───────────────────┐
        │    :core:data     │                               │     :core:ui      │
        └─────────┬─────────┘                               └───────────────────┘
                  │
                  ▼
        ┌───────────────────┐
        │   :core:domain    │
        └───────────────────┘
```
- **Benefits**:
  - **Build Parallelism & Incremental Compilation**: Modifying a single feature (e.g. Checkout) compiles only that module, drastically cutting build times.
  - **Team Independence & Code Ownership**: Separate teams can work on `:feature:checkout` or `:feature:orders` without merge friction.
  - **Dynamic Feature Delivery**: Modules can be downloaded on-demand using Play Feature Delivery to keep base APK size lean.

### 4.3 Future Server-Driven UI (SDUI) Strategy
To give e-commerce merchandising teams complete real-time control over home screens, sale banners, and layout ordering without waiting for Play Store app releases:
- **Compose SDUI Engine**: Declarative Jetpack Compose is uniquely suited for **Server-Driven UI**. The backend returns a dynamic JSON component tree describing layout structure, banner colors, product carousels, and action triggers.
- **Dynamic Component Parser**: A lightweight client parser maps JSON component types (`"HERO_BANNER"`, `"GRID_2_COL"`, `"HORIZONTAL_CAROUSEL"`) to Compose composables, enabling instantaneous, zero-deployment promotional updates.

---

## 5. Next Steps (With Another Day)
1. **Offline-First Storage (Room DB)**:
   - Implement local SQLite tables for products, categories, search history, and sales enquiry drafts.
2. **Multi-Module Refactoring**:
   - Extract `:core:domain`, `:core:data`, `:core:ui`, and feature modules (`:feature:home`, `:feature:detail`, etc.).
3. **Server-Driven UI (SDUI) Prototype**:
   - Build a JSON-driven Compose layout renderer for dynamic home screen promotion sections.
4. **Jetpack Paging 3 Integration**:
   - Add infinite scrolling pagination for high-volume product listings.
5. **Comprehensive Automated Test Suite**:
   - Add Jetpack Compose UI tests (`ComposeTestRule`), End-to-End navigation tests, and network mocking with `MockWebServer`.
