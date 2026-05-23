# ComposeTrendingMovies

ComposeTrendingMovies is a modern Android application that showcases trending movies using the TMDB API. It is built with the latest Android development tools and best practices, featuring a fully declarative UI with Jetpack Compose.

## Project Overview

This project serves as a demonstration of a clean architecture approach, utilizing MVVM for separation of concerns and Hilt for robust dependency injection. It fetches movie data from TMDB and provides a seamless user experience for browsing and viewing movie details.

### Key Features

- **Trending Movie List**: A scrollable list of currently popular movies, including titles, posters, and genres.
- **Detailed Movie View**: In-depth information about a specific movie, including its overview and a high-resolution poster.
- **Offline Support**: Basic caching mechanism using Room (integrated via the data library) to allow viewing previously loaded movies without an active internet connection.
- **Responsive UI**: Built with Jetpack Compose, the UI adapts to different screen sizes.

### Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android's modern toolkit for building native UI.
- **Architecture**: MVVM (Model-View-ViewModel) with [UI State management](https://developer.android.com/topic/libraries/architecture/viewmodel).
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - A library for dependency injection that reduces the boilerplate of manual DI.
- **Navigation**: [Jetpack Navigation (Type-Safe)](https://developer.android.com/guide/navigation/design/type-safety) - For navigating between screens with type safety.
- **Networking & Data**:
    - [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html) - For asynchronous operations and reactive data streams.
    - TMDB API Integration - For fetching real-time movie data.
    - [Room](https://developer.android.com/training/data-storage/room) - For local data persistence.
- **Image Loading**: [Glide (Compose Integration)](https://github.com/bumptech/glide) - For efficient image loading and caching.

## Project Structure

- `presentation/movieslist`: Contains the UI and ViewModel for the trending movies screen.
- `presentation/moviedetail`: Contains the UI and ViewModel for the movie details screen.
- `presentation/navigation`: Defines the type-safe navigation routes.
- `di`: Hilt modules for providing dependencies like services and databases.
- `utils`: Utility classes and constants.

## Setup

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle and run the app on an emulator or physical device.
