# 🎮 ComposeTetris (KMP + Compose Multiplatform)

This is a modern, production-ready Tetris game built using **Kotlin Multiplatform** and **Compose Multiplatform**. It supports **Android, Desktop (JVM), and iOS** from a single shared codebase.

## 🚀 Current Status

I have completed the core implementation of the Tetris engine and the primary UI components. The game is playable on Android and Desktop, with the iOS target sharing the same logic and UI.

### ✅ What's Done

#### **Core Logic & Engine (Phases 1-8)**
- **Architecture:** Clean Architecture + MVVM with Unidirectional Data Flow.
- **Domain Models:** Immutable `Board`, `Cell`, `Tetromino`, and `Offset` models.
- **Game Engine:** Coroutine-based game loop with `StateFlow` as the single source of truth.
- **Super Rotation System (SRS):** Full implementation of SRS with all 8 wall-kick transitions for every piece type.
- **7-Bag Randomizer:** Official piece generation algorithm for fair play.
- **Collision Detection:** Accurate boundary and piece-to-piece collision handling.
- **Game Mechanics:** Line clearing, scoring (Single, Double, Triple, Tetris), level progression, soft/hard drop, and hold piece.
- **Ghost Piece:** Logic and rendering to show landing positions.
- **Next Piece Queue:** Preview of the next tetromino to spawn.

#### **User Interface (Compose Multiplatform)**
- **LCD Aesthetic:** Retro greenish-yellow background and brick-style block rendering matching the reference design.
- **Game Screen:** Integrated Board, HUD (Score, Level, Lines), and Next/Hold previews.
- **Controls:** 
    - **Android:** On-screen touch controls (D-Pad style).
    - **Desktop:** Keyboard support (mapped via common `GameAction` dispatch).
- **Overlays:** Start and Game Over screens.

### 🔄 Missing / Next Steps

#### **Animations & Audio (Phase 9)**
- [ ] **Animations:** Line clear effects, piece locking flashes, and UI transitions.
- [ ] **Audio:** `expect/actual` abstraction for background music and sound effects on Android, iOS, and Desktop.

#### **Persistence & Features (Phase 10)**
- [ ] **High Scores:** Storing best scores locally.
- [ ] **Settings:** Music/Sound toggles and theme preferences.
- [ ] **Responsiveness:** Optimizing layout for Landscape, Tablet, and Desktop window resizing.

#### **Verification (Phase 11)**
- [ ] **Unit Tests:** Repairing and expanding the test suite in `commonTest`.
- [ ] **iOS Verification:** Deep verification of the Swift entry point and framework integration.

---

## 🛠️ Project Structure

* [/shared](./shared/src) - **Shared Module (Core)**
    - `game/logic` - Stateless game rules (SRS, Collisions).
    - `game/engine` - State management and game loop.
    - `game/model` - Immutable data structures.
    - `ui/presentation` - Compose Multiplatform screens and ViewModels.
    - `ui/theme` - Colors and custom styling.
* [/androidApp](./androidApp) - Android specific entry point.
* [/desktopApp](./desktopApp) - JVM/Desktop entry point.
* [/iosApp](./iosApp) - iOS/Swift entry point.

## 🏗️ Running the apps

### Android
`./gradlew :androidApp:assembleDebug`

### Desktop
`./gradlew :desktopApp:run`

### iOS
Open `iosApp/iosApp.xcworkspace` in Xcode and run.

---

## 📚 References
Inspired by the [vitaviva/compose-tetris](https://github.com/vitaviva/compose-tetris) repository, but reimplemented from scratch using modern KMP best practices.
