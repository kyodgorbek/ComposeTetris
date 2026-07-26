# 🎮 ComposeTetris (KMP + Compose Multiplatform)

A modern, production-ready Tetris game built using **Kotlin Multiplatform** and **Compose Multiplatform**. It supports **Android, Desktop (JVM), and iOS** from a single shared codebase.

## 🚀 Current Status: Complete

The project is fully implemented according to the product specification. It features a robust game engine, authentic gameplay mechanics, and a polished retro UI.

### ✅ What's Done (Detailed Implementation)

#### **1. Architecture & Core Logic**
- **Clean Architecture:** Strictly followed MVVM and Unidirectional Data Flow.
- **Game Engine:** Coroutine-based game loop running in `viewModelScope`, emitting immutable `GameState`.
- **Super Rotation System (SRS):** Full implementation of official SRS with wall-kick tables for all piece types, enabling professional-level maneuvers.
- **7-Bag Randomizer:** Ensures fair and standard piece distribution.
- **Stateless Game Logic:** Isolated rules for collision detection, movement, and scoring, making the code highly testable.

#### **2. Gameplay Mechanics**
- **Line Clearing:** Integrated with an asynchronous animation state to provide visual feedback.
- **Scoring System:** Implements standard scoring (1-4 lines) with level-based multipliers.
- **Piece Handling:** Supports Soft Drop, Hard Drop, and Hold Piece mechanics.
- **Ghost Piece:** Real-time landing prediction to assist players.
- **Game Progression:** Dynamic speed increase based on lines cleared.

#### **3. User Interface (Compose Multiplatform)**
- **Retro LCD Aesthetic:** Custom Canvas-based rendering with a greenish-yellow background and "brick" block styling.
- **Cross-Platform HUD:** Displays Score, Level, Lines, Next/Hold previews, and a Top 5 High Score leaderboard.
- **Adaptive Layout:** Uses weights and aspect ratios to ensure the game looks perfect on any screen size.
- **Controls:**
    - **Android:** Virtual D-Pad on-screen controls.
    - **Desktop:** Physical keyboard mapping (Arrow keys, Space, Shift).
- **Animations:** Flashing line clear effects and piece lock visual feedback.

#### **4. Persistence & Settings**
- **Local Storage:** Uses `multiplatform-settings` and `kotlinx-serialization` to persist high scores and user preferences.
- **Settings UI:** A dedicated dialog to toggle sound effects and manage game options.

#### **5. CI/CD & Testing**
- **GitHub Actions:** Automated pipeline for:
    - **Shared Tests:** Running unit tests on every push.
    - **Multi-Platform Builds:** Generating Android APKs and Desktop binaries for Linux, macOS, and Windows.
- **Unit Test Suite:** Comprehensive tests for `RotationLogic` (SRS), `GameLogic` (Engine), and `GameEngine` (State).

---

## 🛠️ Project Structure

* [/shared](./shared/src) - **Shared Module (Core)**
    - `game/logic` - Stateless rules (SRS, Collisions).
    - `game/engine` - State management and loop.
    - `game/model` - Immutable models (`Board`, `Tetromino`).
    - `ui/presentation` - Compose screens and ViewModels.
    - `util` - `AudioManager`, `PreferenceManager`, and time utilities.
* [/androidApp](./androidApp) - Android entry point.
* [/desktopApp](./desktopApp) - JVM Desktop entry point.
* [/iosApp](./iosApp) - iOS Swift entry point.

## 🏗️ How to Run

### Desktop
```bash
./gradlew :desktopApp:run
```

### Android
```bash
./gradlew :androidApp:assembleDebug
```

### iOS
Open `iosApp/iosApp.xcworkspace` in Xcode and run on a simulator or device.

---

## 📚 References
Inspired by the [vitaviva/compose-tetris](https://github.com/vitaviva/compose-tetris) repository, but modernized with the latest KMP stack (Kotlin 2.4.10, Compose 1.11.1).
