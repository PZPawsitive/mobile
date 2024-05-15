package com.example.pawsitive.walkactivityviews

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTPeripheral


@Composable
fun WalkNavGraph(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit
) {
    NavHost(navController = navController, startDestination = RootScreen.Info.route) {
        addInfoRoute(navController, beaconViewModel, refresh, connect, disconnect)
        addMapRoute(navController)
        addMessagesRoute(navController)
    }
}

private fun NavGraphBuilder.addInfoRoute(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit
) {
    navigation(route = RootScreen.Info.route, startDestination = LeafScreen.Info.route) {
        showInfo(navController, beaconViewModel, refresh, connect, disconnect)
        showDeviceConnected(navController, beaconViewModel, disconnect)
    }
}

private fun NavGraphBuilder.addMapRoute(
    navController: NavHostController,
) {
    navigation(route = RootScreen.Map.route, startDestination = LeafScreen.Map.route) {
        showMap(navController)
    }
}

private fun NavGraphBuilder.addMessagesRoute(navController: NavHostController) {
    navigation(route = RootScreen.Messages.route, startDestination = LeafScreen.Messages.route) {
        showMessages(navController)
    }
}

private fun NavGraphBuilder.showInfo(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit
) {
    composable(route = LeafScreen.Info.route) {
        InfoScreen(beaconViewModel, refresh, connect, disconnect, navController)
    }
}

private fun NavGraphBuilder.showDeviceConnected(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    disconnect: (MTPeripheral) -> Unit
) {
    composable(route = LeafScreen.DeviceConnected.route) {
        DeviceConnectedScreen(beaconViewModel = beaconViewModel, navController, disconnect)
    }
}

private fun NavGraphBuilder.showMap(
    navController: NavHostController
) {
    composable(route = LeafScreen.Map.route) {
        MapScreen()
    }
}

private fun NavGraphBuilder.showMessages(navController: NavHostController) {
    composable(route = LeafScreen.Messages.route) {
        MessageScreen()
    }
}
