# GitHub User Search App

A modern **Jetpack Compose** Android application to search GitHub users, view their profiles, and
list their repositories with infinite scrolling.

---

## Features

- Search GitHub users by username.
- Display user profile with:
    - Avatar
    - Username
    - Bio
    - Followers count
    - Public repositories count
- Infinite scroll for repositories list (load more on scroll).
- Modern Material 3 UI with colored cards for repo items.
- Network connectivity check with error handling.

---

---

## Technologies & Libraries

- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **ViewModel + LiveData** - MVVM architecture
- **Coroutines** - Asynchronous network calls
- **Retrofit** - GitHub API integration
- **Navigation Compose** - In-app navigation
- **Material 3** - Modern UI components

---

## Project Structure

com.example.githubusersearch
│
├─ app # Main Composable entry
├─ screens # Composables for Search and Profile
├─ viewModel # MVVM ViewModels for Users & Repos
├─ data
│ ├─ models # Data models (UsersDetails, RepoDetails)
│ └─ repository # API repository (AppRepository)
├─ utils # ApiCallback, NetworkUtils, CommonUtils
└─ Component # Reusable Composable UI components

# API Token
Set your API token in `local.properties`: