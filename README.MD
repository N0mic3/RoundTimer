# RoundTimer

Round Timer is a native Android interval timer app built as a recap project for practicing modern Android development. Users can customize work duration, rest duration, and round count; run a work/rest timer; and manage named timer presets through local Room database storage.
The app follows MVVM and Clean Architecture principles, uses Hilt for dependency injection, Jetpack Compose and Navigation 3 for UI/navigation, Retrofit for the ZenQuotes Quote of the Day API, and typed DataStore to cache the daily quote locally.
## Features

- Choose a work duration from 5 to 60 seconds
- Choose a rest duration from 5 to 60 seconds
- Choose between 1 and 10 rounds
- Ready countdown before the workout begins
- Work and rest phases
- Automatically completes after the final work round
- Pause, resume, and reset the timer
- Navigation between setup and timer screens
- Fetch a Quote of the Day from the ZenQuotes API.
- Caches the Quote of the Day locally with typed DataStore to avoid unnecessary API calls.
- Uses Hilt for dependency injection. 
- Save named timer presets locally.
- View, update, and delete saved timer presets.
- Start a timer directly from a saved preset.

## Built With

- Kotlin
- Jetpack Compose
- Navigation 3
- Kotlin Serialization
- Clean Architecture
- MVVM
- retrofit
- DataStore
- Hilt
- Room

## How It Works

1. Select your work duration, rest duration, and number of rounds.
2. Tap **Start**.
3. The app displays a short “Get Ready” countdown.
4. Complete each work and rest interval.
5. Pause or resume whenever needed.
6. After the final work interval, the timer shows **Done**.

## Getting Started

1. Clone this repository:

   ```bash
   git clone https://github.com/n0mic3/RoundTimer.git
   ```

2. Open the project in Android Studio.

3. Let Gradle finish syncing.

4. Run the app on an Android emulator or physical Android device.

## Project Structure

```text
app/src/main/java/com/example/roundtimer/
├── model/       # Timer data and navigation screen keys
├── ui/
│   ├── components/ # Reusable Compose UI components
│   ├── screens/    # Setup and running timer screens
│   └── theme/      # App colors, typography, and theme
├── utils/       # Shared helper functions
└── MainActivity.kt
```

## Future Improvements

- Add sound or vibration when a phase ends
- Improve the UI
- Integrate an AI Kit for a chat feature

## Author

Yu Hao Liu