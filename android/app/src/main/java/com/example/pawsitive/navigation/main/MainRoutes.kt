package com.example.pawsitive.navigation.main

sealed class MainRootScreen(val route: String) {
    data object Home : MainRootScreen("home_root")
    data object ContractList : MainRootScreen("contract_list_root")
    data object Pet : MainRootScreen("pet_root")
    data object Messages : MainRootScreen("messages_root")
    data object Profile : MainRootScreen("profile_root")
    data object Settings : MainRootScreen("settings_root")
}

sealed class MainLeafScreen(val route: String) {
    data object Home : MainLeafScreen("home")
    data object ContractList : MainLeafScreen("contract_list")
    data object Pet : MainLeafScreen("pet")
    data object Messages : MainLeafScreen("messages")
    data object Chat : MainLeafScreen("chat")
    data object Profile : MainLeafScreen("profile")
    data object PetInfo : MainLeafScreen("pet_info")
    data object PetHistory : MainLeafScreen("pet_history")
    data object PetHistoryMap : MainLeafScreen("pet_history_map")
    data object PetAddForm : MainLeafScreen("pet_add_form")
    data object ContractScreen : MainLeafScreen("contract_screen")
    data object Settings : MainLeafScreen("settings")
}