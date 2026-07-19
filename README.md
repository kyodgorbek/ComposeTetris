This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :desktopApp:hotRun --auto`
  - Standard run: `./gradlew :desktopApp:run`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- Desktop tests: `./gradlew :shared:jvmTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

## 🎮 ComposeTetris Project Plan

This project is implementing a modern, production-ready Tetris game using **Kotlin Multiplatform and Compose Multiplatform**. The implementation is based on the [compose-tetris](https://github.com/vitaviva/compose-tetris) reference repository, with a completely modernized architecture using **Clean Architecture + MVVM**.

### Architecture
- **Clean Architecture** with separation of concerns
- **MVVM** pattern (GameViewModel + GameEngine)
- **Single Source of Truth** using immutable GameState via StateFlow
- **Unidirectional Data Flow** for predictable state management
- **Game Engine** independent of UI layer

### Key Features
- **Game Board:** 20×10 grid with complete Tetris mechanics
- **All 7 Tetrominoes** with Super Rotation System (SRS)
- **Official 7-Bag Randomizer** for piece generation
- **Scoring System:** Single, Double, Triple, Tetris, Combos, Perfect Clears
- **Progressive Levels:** Speed increases every 20 lines cleared
- **Game States:** Play, Pause, Game Over with proper transitions
- **Controls:** Arrow Keys (Desktop), Touch UI (Android/iOS)
- **Persistence:** High scores, settings, statistics

### Implementation Roadmap (11 Phases)
1. ✅ **Setup & Architecture** - Project structure and design
2. ✅ **Domain Models** - Board, Cell, Tetrominoes, Randomizer
3. ✅ **Game State & Engine** - Core game logic and state management
4. 🔄 **Render & Spawn** - UI rendering and piece spawning (IN PROGRESS)
5. **Movement & Collision** - Controls and collision detection
6. **Scoring & Levels** - Line clearing and progression
7. **Queue & Hold** - Next piece preview and hold mechanics
8. **Game State Control** - Pause, resume, restart, game over
9. **Polish & Effects** - Animations and sound
10. **Features & Settings** - Settings, high scores, accessibility
11. **Finalization** - Tests, optimization, documentation

### Phase 2 & 3 Completion ✅

**Phase 2: Domain Models**
- Position, Cell, Board (20×10), Tetromino (all 7 pieces), Randomizer (7-Bag)
- 50+ comprehensive unit tests
- 100% immutability enforcement

**Phase 3: Game State & Engine**
- GameState (immutable single source of truth)
- GameAction (all user/system actions)
- GameEvent (game notifications for UI)
- GameEngine (complete game logic processor)
- CollisionDetector, LineClearer, Scorer
- ~60 FPS Coroutine-based game loop
- Reactive StateFlow + SharedFlow
- 20+ comprehensive unit tests

**Key Features Implemented:**
- ✓ Full collision detection with boundaries
- ✓ Complete scoring (1-4 lines, combos, bonuses)
- ✓ Level progression with dynamic speed
- ✓ Pause/Resume/Restart logic
- ✓ Piece locking and line clearing
- ✓ Game over detection
- ✓ State transitions

See [Phase 3 Summary](./docs/PHASE3_SUMMARY.md) for detailed implementation.

### Quality Standards
- Production-grade code with SOLID principles
- Comprehensive test coverage
- All platforms (Android, Desktop, iOS) compile and run at each phase
- No breaking changes or placeholder implementations
- Immutable models and pure functions preferred

**Progress:** ✅ Phase 1, 2, & 3 Complete → 🔄 Phase 4 (Render & Spawn)

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…