package com.example.pawsitive.navigation.walk

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.pawsitive.view.walk.screens.DeviceConnectedScreen
import com.example.pawsitive.view.walk.screens.InfoScreen
import com.example.pawsitive.view.walk.screens.MapScreen
import com.example.pawsitive.view.walk.screens.MessageScreen
import com.example.pawsitive.viewmodel.ApiViewModel
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTPeripheral


@Composable
fun WalkNavGraph(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit,
    apiViewModel: ApiViewModel,
    historyId: String?
) {
    NavHost(navController = navController, startDestination = WalkRootScreen.Info.route) {
        addInfoRoute(navController, beaconViewModel, refresh, connect, disconnect, apiViewModel, historyId)
        addMapRoute(navController)
        addMessagesRoute(navController)
    }
}

private fun NavGraphBuilder.addInfoRoute(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit,
    apiViewModel: ApiViewModel,
    historyId: String?
) {
    navigation(route = WalkRootScreen.Info.route, startDestination = WalkLeafScreen.Info.route) {
        showInfo(navController, beaconViewModel, refresh, connect, disconnect, apiViewModel, historyId)
        showDeviceConnected(navController, beaconViewModel, disconnect)
    }
}

private fun NavGraphBuilder.addMapRoute(
    navController: NavHostController,
) {
    navigation(route = WalkRootScreen.Map.route, startDestination = WalkLeafScreen.Map.route) {
        showMap(navController)
    }
}

private fun NavGraphBuilder.addMessagesRoute(navController: NavHostController) {
    navigation(route = WalkRootScreen.Messages.route, startDestination = WalkLeafScreen.Messages.route) {
        showMessages(navController)
    }
}

private fun NavGraphBuilder.showInfo(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit,
    apiViewModel: ApiViewModel,
    historyId: String?
) {
    composable(route = WalkLeafScreen.Info.route) {
        InfoScreen(beaconViewModel, refresh, connect, disconnect, navController, apiViewModel, historyId)
    }
}

private fun NavGraphBuilder.showDeviceConnected(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    disconnect: (MTPeripheral) -> Unit
) {
    composable(route = WalkLeafScreen.DeviceConnected.route) {
        DeviceConnectedScreen(beaconViewModel = beaconViewModel, navController, disconnect)
    }
}

private fun NavGraphBuilder.showMap(
    navController: NavHostController
) {
    composable(route = WalkLeafScreen.Map.route) {
        MapScreen()
    }
}

private fun NavGraphBuilder.showMessages(navController: NavHostController) {
    composable(route = WalkLeafScreen.Messages.route) {
        MessageScreen()
    }
}
