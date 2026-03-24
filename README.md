# Toy Tools

A growing collection of Android utility toys, built with Jetpack Compose, Room, and MVVM architecture.

Each toy lives in its own self-contained module under `toy/`, sharing a common database, theme, and core UI components from `core/`.

---

## Toys

| Toy | Description |
|-----|-------------|
| **Todo** | Create, complete, and delete tasks with persistent local storage |

> More toys will be added here as the app grows.

---

## Architecture

The project follows **MVVM + Repository** pattern with a toy-based package structure.

```
app/src/main/java/web/athma/toytools/
├── MainActivity.kt               # Single Activity — bootstraps deps, hosts all toy screens
├── core/
│   ├── data/
│   │   └── database/
│   │       └── ToyToolsDatabase.kt  # Shared Room database (all toy entities registered here)
│   └── ui/
│       ├── theme/                  # App-wide Material3 theme, colors, typography
│       └── components/             # Reusable UI: CoreViewModel, AppAlertDialog, DialogState
└── toy/
    └── todoApp/
        ├── data/                   # Todo entity, DAO, Repository
        └── ui/                     # TodoScreen, TodoViewModel, TodoViewModelFactory
```

### Data flow

```
UI (Composable) → ViewModel → Repository → DAO → Room Database
                      ↑                              |
                      └──────────── Flow ────────────┘
```

- **Room** handles persistence; DAOs return `Flow<T>` for reactive updates.
- **Repository** is the single source of truth — ViewModels never call DAOs directly.
- **ViewModel** holds UI state and exposes actions; it survives configuration changes.
- **CoreViewModel** is Activity-scoped and shared across all toys for global UI (e.g. confirmation dialogs).

---

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material Design 3
- **Database:** Room (SQLite) with KSP code generation
- **Async:** Kotlin Coroutines + Flow
- **Architecture:** MVVM + Repository pattern
- **Min SDK:** 24 | **Target SDK:** 34

---

## Setup

1. Clone the repository.
2. Open in **Android Studio Hedgehog (2023.1.1)** or later.
3. Let Gradle sync and download dependencies.
4. Run on a device or emulator with **API 24+**.

No API keys or external services are required — all data is stored locally on the device.

---

## Adding a New Toy

1. Create a new package under `toy/yourToyName/`.
2. Add `data/` with your Room entity, DAO, and Repository.
3. Register the entity in `ToyToolsDatabase` and bump the database `version`.
4. Add `ui/` with your Screen composable, ViewModel, and ViewModelFactory.
5. Wire up dependencies in `MainActivity` and add the screen to the content area.
6. Document public classes and functions with KDoc.
