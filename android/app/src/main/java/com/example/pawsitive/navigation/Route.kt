package com.example.pawsitive.navigation

sealed class RootScreen(val route: String) {
    data object Home : RootScreen("home_root")
    data object Follow : RootScreen("follow_root")
    data object Pet : RootScreen("pet_root")
    data object Search : RootScreen("search_root")
    data object Favorites : RootScreen("favorites_root")
    data object Profile : RootScreen("profile_root")
}

sealed class LeafScreen(val route: String) {
    data object Home : LeafScreen("home")
    data object Follow : LeafScreen("follow")
    data object Pet : LeafScreen("pet")
    data object Search : LeafScreen("search")
    data object Favorites : LeafScreen("favorites")
    data object Profile : LeafScreen("profile")
    data object HomeDetail : LeafScreen("home_detail")
    data object BookReader : LeafScreen("book_reader")
}