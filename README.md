# Movies Watchlist Android App

An Android application built with Java that allows users to search, browse, and manage a personal watchlist of movies. It fetches data from an external movie API and provides an intuitive interface for organizing favorite titles.

---

## Contents

- [Features](#features)  
- [Tech Stack](#tech-stack)  
- [Getting Started](#getting-started)   

---

## Features

- Search movies using a public API (e.g., TMDB)
- View movie details: title, release date, rating, poster, and overview
- Add or remove movies from your personal watchlist
- Persistent storage and authentication using Firebase

---

## Tech Stack

- **Java**  
- **Android SDK**  
- **Firebase** – for user authentication and watchlist storage  
- **Retrofit** – for REST API communication  
- **RecyclerView** – for displaying movie lists  
- **Glide** – for loading movie posters  

---

## Getting Started

1. **Clone the repository:**

```bash
git clone https://github.com/yourusername/android-movies-watchlist
```

2. **Open in Android Studio:**
   
File → Open → Navigate to the project folder

3. **Add your API key:**
   
Replace INSERT_API_KEY with your TMDB API key (in ExploreActivity.java and SearchActivity.java).

4. **Build & Run**
   
Run the app on an emulator or physical Android device.
