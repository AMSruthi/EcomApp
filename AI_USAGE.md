# AI Usage & Engineering Notes

This document details how AI tooling was directed and utilized during the development of EcomApp.

---

## 1. Tools and Models Used
- **AI Coding Assistant**: Android Studio AI Assistant / Gemini-powered IDE agent.
- **Workflow**: Iterative prompt direction, architectural scaffolding, Hilt DI setup, automated test generation, and device verification.

---

## 2. Engineering Process & Direction
- **Phase 1 (Planning & Architecture)**: Instructed the AI to establish Clean Architecture boundaries (Domain, Data, Presentation) and a feature-based package layout (`ui.home`, `ui.category`, `ui.search`, `ui.detail`, `ui.common`).
- **Phase 2 (Implementation & Hilt DI)**: Guided the creation of Retrofit networking, isolated DTO models (`ProductDto`, `RatingDto`, `EnquiryPayload`), Hilt modules (`NetworkModule`, `RepositoryModule`), feature ViewModels with `@HiltViewModel`, and Jetpack Compose screens.
- **Phase 3 (Testing & Verification)**: Directed unit test creation using Kotlin coroutine test dispatchers and validated builds on the Android emulator.

---

## 3. Key Prompts & Shaping Moments
1. *"Design a Clean Architecture MVVM structure in Kotlin for an e-commerce catalogue using Jetpack Compose, Hilt DI, and Retrofit."* -> Established core package hierarchy, DI modules, and repository pattern.
2. *"Refactor UI states from a single monolithic state into screen-specific state classes (`HomeUiState`, `CategoryUiState`, `SearchUiState`, `ProductDetailUiState`) and split monolithic ViewModel into feature-specific ViewModels with `@HiltViewModel`."* -> Encapsulated feature states and reduced ViewModel complexity.
3. *"Align Gradle version catalog dependencies with KSP 2.3.14 and Hilt 2.60.1 for Kotlin 2.2."* -> Configured robust compile-time annotation processing with KSP.

---

## 4. AI Missteps & Course Correction
- **Material Icons Package**: Initially suggested outdated icon references (`Icons.Default.Search`). Caught during compilation (`Unresolved reference`) and corrected to use `Icons.Filled.Search`.
- **Hilt Plugin Configuration**: Caught plugin ordering issues when combining AGP 9.4.1 and Kotlin Compose, switching to version catalog `ksp` and `hilt` plugin aliases matching modern Android standards.

---

## 5. Time Spent Breakdown
- **Planning & Architecture Setup**: 0.5 hours
- **Data, Domain, & Hilt DI Layer**: 0.75 hours
- **UI & Navigation (Jetpack Compose)**: 1.0 hours
- **Unit Testing & Documentation**: 0.25 hours
- **Total Time**: ~2.5 hours
