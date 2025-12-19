URL Browser
===========

A lightweight Android app to quickly open and browse website URLs with a simple, modern UI, built using Kotlin, MVVM, and WebView.

Features
--------

*   Enter any website URL and open it in an in-app WebView.
    
*   URL validation with meaningful error messages for invalid or empty input.
    
*   Loading indicator that reflects real-time page load progress.
    
*   Displays the currently loaded URL in the WebView toolbar.
    
*   Automatically saves visited URLs into a local history list.
    
*   Dedicated History screen to view and manage previously visited URLs.
    
*   Option to clear all browsing history from local storage.
    
*   Option to upload browsing history to a mock REST API (Beeceptor endpoint).
    
*   Smooth navigation between Home, WebView, and History screens.
    

Tech Stack
----------

*   **Language:** Kotlin
    
*   **Architecture:** MVVM (ViewModel + LiveData)
    
*   **UI:** XML layouts, Material Components, ViewBinding
    
*   **Navigation:** Jetpack Navigation Component
    
*   **Storage:** Room Database for URL history
    
*   **Networking:** Retrofit + Gson (Beeceptor mock API)
    
*   **Web:** Android WebView
    
*   **Async:** Kotlin Coroutines (viewModelScope)
    

Screens Overview
----------------

Home Screen
-----------

*   Carousel banner at the top using ViewPager2 and dots indicator.
    
*   Text input field to enter a website URL with inline error handling.
    
*   “Open” button to navigate to the WebView screen with the validated URL.
    
*   Toolbar action to navigate to the History screen.
    

WebView Screen
--------------

*   Displays the current URL in a toolbar.
    
*   In-app WebView with JavaScript and DOM storage enabled.
    
*   Progress bar showing page loading progress.
    
*   Back button: returns to Home while retaining the last opened URL in the input field.
    
*   Close button: returns to Home and clears the URL input field.
    

History Screen
--------------

*   RecyclerView listing all previously visited URLs with their timestamps.
    
*   Empty state when no history exists.
    
*   Action to clear all saved history from the local Room database.
    
*   Action to upload the complete history list to a Beeceptor mock API endpoint.
    

How It Works (Architecture)
---------------------------

*   **MVVM:**
    
    *   ViewModels (HomeViewModel, WebViewViewModel, HistoryViewModel) expose LiveData for UI state.
        
    *   Fragments observe these LiveData objects and update the UI reactively.
        
*   **Room:**
    
    *   UrlHistoryEntity represents each visited URL and its timestamp.
        
    *   UrlHistoryDao provides methods to insert, query, and clear history.
        
    *   UrlHistoryDatabase exposes a singleton Room database instance.
        
*   **Repository:**
    
    *   UrlRepository abstracts access to Room and provides a clean API to ViewModels.
        
*   **Networking:**
    
    *   ApiService defines the uploadHistory endpoint for history upload.
        
    *   RetrofitClient configures Retrofit with the Beeceptor base URL.
        
    *   History is uploaded as a list of URL items in a single POST request.
        
*   **Validation:**
    
    *   UrlValidator validates and normalizes URLs (e.g., auto-adding https:// when missing).
        
    *   Returns a sealed ValidationResult with either the formatted URL or an error message.
