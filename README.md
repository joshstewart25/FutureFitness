# Fitness Stats

An Android app that lets a coach review how their clients are training. Pick a client, see what they have coming up this week, browse their workout history, and open any past workout to see how it went: heart rate, effort, notes, where it happened, and every set that was performed.

The app started as Future's "Exercise Progress" Android project and runs entirely on bundled sample data, so there is no backend or sign-in.

## What it does

The app has four screens:

**Welcome** lists the coach's clients, most recently active first. Each card shows the date of the client's last completed workout. Clients with no completed workouts are listed last, alphabetically.

**Client detail** shows the client's workouts for today and the next six days. Rest days appear as "Recovery Day" with a little falling confetti. A button leads to the full history.

**Previous workouts** lists everything the client has already done, newest first. Each card is labeled with its status, and the cards that need attention are tinted as well:

- **Completed**: finished the workout.
- **Not completed**: started it but never finished.
- **Missed**: never started and marked as missed.

The list can be narrowed with status chips (pick any combination) and a date range picker, and a single button clears the filters.

**Workout detail** is a dashboard for one workout. A section only appears when the workout has data for it:

- A heart rate chart over the whole workout, with the peak marked.
- Max and average heart rate, energy burned and duration.
- The client's notes after the workout.
- How hard the workout felt, from easy to hard.
- A map pinning where the workout took place (OpenStreetMap, needs an internet connection).
- The sets performed, grouped by workout section. Tap a section to expand it and see each set's weight, reps, time or distance, with an icon for completed, partially completed or skipped.

The app supports light and dark themes and draws edge-to-edge behind the system bars.

## Tech stack

- Kotlin, Jetpack Compose and Material 3
- Navigation Compose with type-safe routes
- Hilt for dependency injection
- kotlinx.serialization for reading the JSON data
- [Vico](https://github.com/patrykandpatrick/vico) for the heart rate chart
- [osmdroid](https://github.com/osmdroid/osmdroid) for the map (no API key needed)

Versions are all in [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

## Architecture

The app is a single activity built with MVVM and unidirectional data flow. Each screen has a ViewModel that builds an immutable `UiState` from the repositories and exposes it as a `StateFlow`. The screen reads it with `collectAsStateWithLifecycle()` and reports user actions back to the ViewModel.

Screens come in pairs: a public `XScreen` that connects the ViewModel, and a private `XContent` that only draws the state it is given.

```
app/src/main/java/co/future/exerciseprogress/
├── data/           Repositories and the serializable models
├── di/             Hilt module
├── ui/
│   ├── navigation/ NavHost and the route definitions
│   ├── welcome/
│   ├── clientdetail/
│   ├── previousworkouts/
│   ├── workoutdetail/
│   ├── components/ Shared pieces (ScreenScaffold, date range picker, confetti, ...)
│   └── theme/
└── utils/          DateRange and small extension functions
```

Every feature folder holds its screen, ViewModel and `UiState`, plus any cards or widgets only that screen uses. Every screen is wrapped in `ScreenScaffold`, which handles the system bar insets for edge-to-edge drawing.

## Data

The sample data lives in [`app/src/main/assets`](app/src/main/assets):

| Path | Contents |
| --- | --- |
| `clients.json` | The list of clients |
| `workouts.json` | An index of every workout |
| `workouts/<id>.json` | One file per workout, with its sections, sets and exercises |
| `summaries/<id>.json` | One file per attempt at a workout: heart rate, location, notes and set results |

`ClientsRepository` and `WorkoutsRepository` read these files once at startup and keep everything in memory. The ViewModels only talk to the repositories, so swapping in a real data source later only touches the data layer.

A few rules in the data are worth knowing about:

- A workout counts as completed only when one of its summaries has a `full` completion state. Partial or abandoned attempts are ignored for that.
- If a workout was attempted more than once, the detail screen shows the latest completed attempt, or else the latest one that was started.
- Rest sets are hidden from the set list. When a set has no recorded reps or weight, the planned values are shown instead.
- Set summaries whose set has the all-zero "nil" UUID are dropped when loading.

## Known limitations

- **"Today" is fixed.** The sample workouts are all from 2020, so the app treats 2020-11-24 as today (see `AppModule.provideClock`). Switching to the real clock is a one-line change once there is current data.
- **The greeting name is hard-coded.** The welcome screen says "Welcome, Josh" until there is a user model.
- **Data is loaded up front.** The repositories read all the JSON at startup instead of on demand, which is what causes the short wait at startup. A real backend or database would replace this.
- **No per-exercise progress view yet.** The original brief asked to pick an exercise and see its performance over time. The workout detail shows each workout's sets, but nothing tracks one exercise across workouts.

## Development

The project was built with Claude Code. The conventions it follows (MVVM, readable code over clever code, light comments, edge-to-edge screens) are written down in [`CLAUDE.md`](CLAUDE.md).
