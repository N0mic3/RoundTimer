# RoundTimer

[![Android CI](https://github.com/N0mic3/RoundTimer/actions/workflows/android-ci.yml/badge.svg?branch=main)](https://github.com/N0mic3/RoundTimer/actions/workflows/android-ci.yml)

RoundTimer is a native Android interval timer app built as a recap project for practicing modern Android development. Users can configure work duration, rest duration, and round count; run work/rest timer sessions; manage named presets; view a daily quote; receive AI-powered focus suggestions; and opt in to daily focus reminders.

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
- Schedule an opt-in daily focus reminder with WorkManager and a Hilt-injected `CoroutineWorker`.
- Persist the daily-reminder preference with Preferences DataStore and manage it from a Compose settings screen.
- Sign in with Google through Firebase Authentication.
- Use AI Coach for concise focus and timer suggestions in **Cloud** or **On-device** mode.
- Generate on-device AI Coach replies with the ML Kit GenAI Prompt API on compatible devices.
- Check whether the on-device model is available, downloadable, downloading, or unsupported, and let eligible users download it in the app.
- Stream on-device reply text into the chat while it is generated, then save the completed reply to the conversation.
- Secure Cloud AI Coach requests with Firebase Callable Cloud Functions and server-side Firebase UID authorization.
- Protect the Gemini API key and authorized UID with Google Cloud Secret Manager.
- Generate AI Coach replies through the Gemini Developer API without exposing credentials in the Android APK or source control.
- Disable AI Coach navigation in release builds through a local `BuildConfig` feature flag.

## AI Coach Architecture

```text
Android AI Coach UI
        ↓
CoachEngine (domain contract)
        ↓
CoachEngineCoordinator
   ┌────────────┴─────────────┐
Cloud mode                On-device mode
   ↓                          ↓
Firebase Authentication    ML Kit GenAI Prompt API
   ↓                          ↓
Callable Cloud Function    Device-provided model / AICore
   ↓                          ↓
Gemini Developer API       Streamed AI response
```

Cloud mode sends only the user message. The Firebase Function validates the Firebase user, verifies the authorized UID, retrieves the Gemini API key from Secret Manager, and returns only the generated reply.

On-device mode does not call the Firebase backend. It checks the ML Kit model status before generation, supports an in-app download when the model is downloadable, and emits generating, partial-response, completed, and error states. Devices without the required on-device capability show a clear unavailable state.

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
   - Settings screen reminder-switch display and toggle behavior

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
- ML Kit GenAI Prompt API
- On-device Gemini Nano / AICore capability
- TypeScript
- Node.js
- JUnit 4
- MockK
- Kotlin Coroutines Test
- GitHub Actions
- Android Foreground Service
- Android Notifications
- SoundPool
- WorkManager
- Hilt Work
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
8. Choose **Cloud** or **On-device** AI Coach mode, then receive a focus suggestion based on your available time and goal.
9. In on-device mode, download the model from the app first if it is available for download on the current device.
10. Enable the optional daily focus reminder from Settings to receive a notification approximately 24 hours later.

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

> Cloud AI Coach requires your own Firebase and Gemini configuration. On-device AI Coach requires a compatible physical Android device with the required system AI capability; it may be unavailable on emulators or unsupported devices.

> `google-services.json` and Cloud Function secrets are intentionally excluded from source control. Forks must connect to their own Firebase project before deploying or using Cloud AI Coach.

## Project Structure

```text
app/src/
├── main/java/com/example/roundtimer/
│   ├── data/
│   │   ├── audio/                   # Timer phase sound playback
│   │   ├── cloud/                   # Firebase-backed Cloud AI Coach engine
│   │   ├── controller/              # Android timer-session implementation
│   │   ├── local/
│   │   │   ├── datastore/           # Quote cache and app settings
│   │   │   └── room/                # Saved-timer database, DAO, and entities
│   │   ├── notification/            # Foreground timer and daily-reminder notifications
│   │   ├── ondevice/                # ML Kit on-device Coach engine and model availability
│   │   ├── remote/                  # ZenQuotes API and DTOs
│   │   ├── repository/              # Firebase, quote, and timer repositories
│   │   ├── service/                 # Foreground timer service
│   │   └── workmanager/             # Daily focus reminder worker and scheduler
│   ├── di/                          # Hilt modules, including Firebase dependencies
│   ├── domain/
│   │   ├── controller/              # Timer, reminder, and Coach-engine abstractions
│   │   ├── model/                   # Timer, quote, auth, AI Coach, and app-settings models
│   │   ├── repository/              # Repository contracts
│   │   └── usecase/                 # Feature use cases
│   ├── ui/
│   │   ├── components/              # Reusable Compose components
│   │   ├── navigation/              # Navigation 3 graph and navigation keys
│   │   ├── screens/
│   │   │   ├── aiCoachScreen/       # AI Coach MVI state, intents, and ViewModel
│   │   │   ├── running/             # Active-timer screen and ViewModel
│   │   │   ├── savedTimers/         # Saved-presets screen and ViewModel
│   │   │   ├── settings/            # Daily reminder settings screen and ViewModel
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
    │   ├── SettingsScreenTest.kt
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
- Add speech-to-text input and text-to-speech playback controls for AI Coach.
- Add Firebase Cloud Messaging notifications.
- Add user-selected reminder times with AlarmManager for an exact-time reminder experience.

## Author

Yu Hao Liu
