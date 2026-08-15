# RoundTimer

[![Android CI](https://github.com/N0mic3/RoundTimer/actions/workflows/android-ci.yml/badge.svg?branch=main)](https://github.com/N0mic3/RoundTimer/actions/workflows/android-ci.yml)

RoundTimer is a native Android interval timer app built as a recap project for practicing modern Android development. Users can configure work duration, rest duration, and round count; run work/rest timer sessions; manage named presets; view a daily quote; and receive AI-powered focus suggestions.

The app applies MVVM and Clean Architecture principles with Hilt dependency injection, Jetpack Compose, Navigation 3, Room, DataStore, Retrofit, Firebase, and Google Cloud services.

## Features

- Configure work duration from 5 to 60 seconds.
- Configure rest duration from 5 to 60 seconds.
- Configure 1 to 20 timer rounds.
- Run work/rest sessions with a ready countdown.
- Pause, resume, and reset an active timer.
- Complete automatically after the final work interval.
- Save, view, rename, delete, and launch named timer presets with Room.
- Fetch the ZenQuotes Quote of the Day through Retrofit.
- Cache the daily quote with typed DataStore to avoid unnecessary API calls.
- Play distinct sounds for work, rest, and completion phases.
- Keep an active timer visible in the background through an Android foreground-service notification.
- Update the ongoing notification with the current timer phase and remaining time.
- Sign in with Google through Firebase Authentication.
- Use AI Coach for concise focus and timer suggestions.
- Secure AI Coach requests with Firebase Callable Cloud Functions and server-side Firebase UID authorization.
- Protect the Gemini API key and authorized UID with Google Cloud Secret Manager.
- Generate AI Coach replies through the Gemini Developer API without exposing credentials in the Android APK or source control.

## AI Coach Architecture

```text
Android AI Coach UI
→ Google Sign-In
→ Firebase Authentication
→ Firebase Callable Cloud Function
→ Firebase UID authorization
→ Google Cloud Secret Manager
→ Gemini Developer API
→ AI response displayed in the app
```

The Android app sends only the user message. The Firebase Function validates the Firebase user, verifies the authorized UID, retrieves the Gemini API key from Secret Manager, and returns only the generated reply.

## Testing Tools

### Unit Testing

- JUnit 4 for unit tests and assertions.
- MockK for mocking dependencies and verifying interactions.
- `kotlinx-coroutines-test` for testing suspend functions and controlling coroutine execution.
- `Clock.fixed()` for deterministic date-based tests.

### Compose UI Testing

- Jetpack Compose UI Test framework for instrumented screen and component tests.
- Tests run against reusable Composables with supplied UI state and callbacks, without requiring real ViewModels, Room, Retrofit, Firebase, or Gemini requests.
- UI coverage includes:
   - `DurationPicker` selection behavior
   - Start screen display, navigation, and save-preset flow
   - Saved Timers display, launch, rename, and delete flows
   - Running timer ready, pause, completion, and reset states
   - AI Coach input and send-intent behavior
   - Google Sign-In button display

## Continuous Integration

GitHub Actions runs the Android CI workflow on every push and pull request targeting `main`.

The workflow:

- Restores `google-services.json` from a GitHub Actions repository secret.
- Runs unit tests.
- Builds the debug APK.
- Uploads unit-test reports and the debug APK as downloadable artifacts.

## Built With

- Kotlin
- Jetpack Compose
- Navigation 3
- MVVM
- Clean Architecture
- Hilt
- Kotlin Coroutines and StateFlow
- Kotlin Serialization
- Retrofit
- Room
- Typed DataStore
- Firebase Authentication
- Firebase Cloud Functions Gen 2
- Google Sign-In / Credential Manager
- Google Cloud Secret Manager
- Gemini Developer API
- TypeScript
- Node.js
- JUnit 4
- MockK
- Kotlin Coroutines Test
- GitHub Actions
- Android Foreground Service
- Android Notifications
- SoundPool
- Jetpack Compose UI Testing
- AndroidX Test

## How It Works

1. Select work duration, rest duration, and number of rounds.
2. Tap **Start**.
3. The app displays a short ready countdown.
4. Complete each work and rest interval.
5. Pause, resume, or reset whenever needed.
6. After the final work interval, the timer shows **Done**.
7. Optionally save the selected configuration as a named timer preset.
8. Use AI Coach to receive a focus suggestion based on your available time and goal.

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/N0mic3/RoundTimer.git
   ```

2. Open the project in Android Studio.

3. Let Gradle finish syncing.

4. To build the Firebase-enabled version, connect the project to your own Firebase project and add:

   ```text
   app/google-services.json
   ```

5. Run the app on an Android emulator or physical Android device.

> `google-services.json`, `.firebaserc`, and Cloud Function secrets are intentionally excluded from source control. Forks must connect to their own Firebase project before deploying or using AI Coach.

## Project Structure

```text
app/src/
├── main/java/com/example/roundtimer/
│   ├── data/
│   │   ├── audio/                   # Timer phase sound playback
│   │   ├── controller/              # Android timer-session implementation
│   │   ├── local/
│   │   │   ├── datastore/           # Quote cache
│   │   │   └── room/                # Saved-timer database, DAO, and entities
│   │   ├── notification/            # Foreground notification updates
│   │   ├── remote/                  # ZenQuotes API and DTOs
│   │   ├── repository/              # Firebase, AI Coach, quote, and timer repositories
│   │   └── service/                 # Foreground timer service
│   ├── di/                          # Hilt modules, including Firebase dependencies
│   ├── domain/
│   │   ├── controller/              # Timer-session abstraction
│   │   ├── model/                   # Timer, quote, auth, and AI Coach models
│   │   ├── repository/              # Repository contracts
│   │   └── usecase/                 # Feature use cases
│   ├── ui/
│   │   ├── components/              # Reusable Compose components
│   │   ├── navigation/              # Navigation 3 graph and navigation keys
│   │   ├── screens/
│   │   │   ├── aiCoachScreen/       # AI Coach MVI state, intents, and ViewModel
│   │   │   ├── running/             # Active-timer screen and ViewModel
│   │   │   ├── savedTimers/         # Saved-presets screen and ViewModel
│   │   │   └── start/               # Timer setup and Quote of the Day screen
│   │   └── theme/                   # Compose theme
│   └── utils/                       # Shared utilities
├── test/java/com/example/roundtimer/
│   └── ...                          # Unit tests: use cases, repositories, and ViewModels
└── androidTest/java/com/example/roundtimer/
    ├── roundTimer/
    │   ├── AiCoachScreenTest.kt
    │   ├── DurationPickerTest.kt
    │   ├── GoogleSignInButtonTest.kt
    │   ├── RunningScreenTest.kt
    │   ├── SavedTimersScreenTest.kt
    │   └── StartScreenTest.kt
    └── ExampleInstrumentedTest.kt   # Default Android instrumented test

functions/
├── src/index.ts                     # Firebase Callable AI Coach backend
├── package.json                     # Node.js and Firebase Functions dependencies
├── tsconfig.json                    # TypeScript configuration
└── .eslintrc.js                     # Backend lint configuration

.github/workflows/
└── android-ci.yml                   # Android CI workflow and Firebase config restore
```

## Future Improvements

- Improve AI Coach chat bubbles, auto-scroll behavior, and plain-text response formatting.
- Return structured timer recommendations from AI Coach.
- Add Firebase Cloud Messaging notifications.
- Add optional daily focus reminders with WorkManager.
- Prepare a Play Store release flavor that disables AI Coach until a public-safe access strategy is available.

## Author

Yu Hao Liu