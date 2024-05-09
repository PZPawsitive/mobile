package com.example.pawsitive.navigation

sealed class RootScreen(val route: String) {
    data object Home : RootScreen("home_root")
    data object Follow : RootScreen("follow_root")
    data object Pet : RootScreen("pet_root")
    data object Messages : RootScreen("messages_root")
    data object Profile : RootScreen("profile_root")
}

sealed class LeafScreen(val route: String) {
    data object Home : LeafScreen("home")
    data object Follow : LeafScreen("follow")
    data object Pet : LeafScreen("pet")
    data object Messages : LeafScreen("messages")
    data object Profile : LeafScreen("profile")
    data object HomeDetail : LeafScreen("home_detail")
    data object PetInfo : LeafScreen("pet_info")
    data object PetHistory : LeafScreen("pet_history")
    data object PetHistoryMap : LeafScreen("pet_history_map")
    data object PetAddForm : LeafScreen("pet_add_form")
}