# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build

# Unit tests (JVM)
./gradlew test
./gradlew testDebugUnitTest          # debug variant only

# Run a single unit test class
./gradlew testDebugUnitTest --tests "web.athma.toytools.toy.todoApp.ui.TodoViewModelTest"

# Instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Test coverage report (HTML + XML in app/build/reports/jacoco/)
./gradlew jacocoTestReport           # depends on testDebugUnitTest

# Lint
./gradlew lint
```

## Vocabulary

Each self-contained utility module in this app is called a **toy**. The word "feature" is not used — always say "toy" when referring to an individual module (e.g. "the Todo toy", "add a new toy").

## Architecture

Single-module Android app (`app/`) using **MVVM + Repository**. All code lives under `web.athma.toytools`.

**Package layout:**
```
web.athma.toytools/
├── MainActivity.kt          # Bootstraps the dependency graph manually (no DI framework)
├── core/
│   ├── data/database/
│   │   └── ToyToolsDatabase # Room database — all toy entities registered here
│   └── ui/
│       ├── components/      # CoreViewModel, AppAlertDialog, DialogState (shared across toys)
│       └── theme/           # Material3 theme, colors, typography
└── toy/
    └── todoApp/
        ├── data/            # Todo entity, TodoDao, TodoRepository
        └── ui/              # TodoScreen, TodoViewModel, TodoViewModelFactory
```

**Key wiring:** `MainActivity` manually constructs `ToyToolsDatabase → TodoDao → TodoRepository → TodoViewModelFactory` and passes them into the composable tree. There is no DI framework (no Hilt/Koin).

**CoreViewModel** is Activity-scoped and shared with all toy screens. It owns the single app-wide `AppAlertDialog` state — toy screens call `coreViewModel.showConfirmation(...)` instead of managing their own dialog state.

**Data flow rules:**
- DAOs return `Flow<T>` — ViewModels collect these flows; the UI reacts automatically to DB changes.
- ViewModels **never** call DAOs directly; all DB access goes through a Repository.
- ViewModels hold UI state and launch coroutines via `viewModelScope`.

## Tech Stack

- **UI:** Jetpack Compose + Material Design 3
- **Database:** Room (SQLite) with KSP code generation
- **Async:** Kotlin Coroutines + Flow
- **Min SDK:** 24 | **Target SDK:** 34

## Adding a New Toy

1. Create `toy/yourToyName/data/` — Room entity, DAO, Repository; register the entity in `ToyToolsDatabase` and bump `version`.
2. Create `toy/yourToyName/ui/` — Screen composable, ViewModel, ViewModelFactory.
3. Wire dependencies in `MainActivity` and add the screen to the content area.
4. Document all public classes and functions with KDoc.

## Testing

- **Unit tests** (`src/test/`) use JUnit4 + MockK + `kotlinx-coroutines-test`. ViewModels are tested by setting `Dispatchers.setMain(UnconfinedTestDispatcher())` in `@Before`.
- **Instrumented tests** (`src/androidTest/`) use Compose test rules + a real in-memory Room database (no mocks for the DB layer). `TodoScreenTest` exercises the full stack end-to-end.
- Room's generated `*_Impl` classes and Compose compiler internals are excluded from JaCoCo coverage (see `coverageExclusions` in `app/build.gradle.kts`).
