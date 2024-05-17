package com.example.pawsitive.navigation.walk



sealed class WalkRootScreen(val route: String) {
    data object Info : WalkRootScreen("info_root")
    data object Map : WalkRootScreen("map_root")
    data object Messages : WalkRootScreen("messages_root")
}

sealed class WalkLeafScreen(val route: String) {
    data object Info : WalkLeafScreen("info")
    data object DeviceConnected : WalkLeafScreen("device_connected")
    data object Map : WalkLeafScreen("map")
    data object Messages : WalkLeafScreen("messages")
}