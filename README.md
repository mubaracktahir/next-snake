# Next Snake - Android Edition

This is a simple Snake game implemented using Kotlin and Jetpack Compose for Android.

## Project Structure

- `app/`: Main application module.
  - `src/main/java/com/example/nextsnake/`: Kotlin source code.
    - `MainActivity.kt`: The main entry point of the application.
    - `model/`: Data classes (GameState, Coordinate, Direction, Constants).
    - `viewmodel/`: ViewModel (GameViewModel) for managing game logic and state.
    - `ui/`: Jetpack Compose UI elements (GameScreen, GameBoard, etc.).
      - `theme/`: Theme definitions (Color, Shape, Theme, Type).
  - `src/main/res/`: Android resources (layouts, drawables, values).
  - `build.gradle.kts`: App-level build script.
  - `AndroidManifest.xml`: Application manifest file.
- `build.gradle.kts`: Top-level build script.
- `settings.gradle.kts`: Gradle settings file.
- `gradle/`: Gradle wrapper files.
- `gradle.properties`: Project-wide Gradle settings.
- `gradle/libs.versions.toml`: Version catalog for dependencies.

## How to Build and Run

1.  Open the project in Android Studio (latest stable version recommended).
2.  Let Gradle sync the project dependencies.
3.  Select an emulator or connect a physical Android device (running Android 5.0 Lollipop or higher).
4.  Click the "Run" button (green play icon) in Android Studio.

## Game Controls

- Use the directional buttons at the bottom of the screen to control the snake.
- Use the central Play/Pause button to start, pause, or resume the game.
- Use the Refresh button in the top app bar to restart the game.
