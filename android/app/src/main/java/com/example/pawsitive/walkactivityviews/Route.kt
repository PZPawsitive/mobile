package com.example.pawsitive.walkactivityviews



sealed class RootScreen(val route: String) {
    data object Info : RootScreen("info_root")
    data object Map : RootScreen("map_root")
    data object Messages : RootScreen("messages_root")
}

sealed class LeafScreen(val route: String) {
    data object Info : LeafScreen("info")
    data object DeviceConnected : LeafScreen("device_connected")
    data object Map : LeafScreen("map")
    data object Messages : LeafScreen("messages")
}