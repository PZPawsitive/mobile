package com.example.pawsitive.walkactivityviews

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.pawsitive.viewmodel.BeaconViewModel


@Composable
fun WalkNavGraph(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit
) {
    NavHost(navController = navController, startDestination = RootScreen.Info.route) {
        addInfoRoute(navController, beaconViewModel, refresh)
        addMapRoute(navController)
        addMessagesRoute(navController)
    }
}

private fun NavGraphBuilder.addInfoRoute(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit
) {
    navigation(route = RootScreen.Info.route, startDestination = LeafScreen.Info.route) {
        showInfo(navController, beaconViewModel, refresh)
    }
}

private fun NavGraphBuilder.addMapRoute(navController: NavHostController) {
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
    refresh: () -> Unit
) {
    composable(route = LeafScreen.Info.route) {
        InfoScreen(beaconViewModel, refresh)
    }
}

private fun NavGraphBuilder.showMap(navController: NavHostController) {
    composable(route = LeafScreen.Map.route) {
        MapScreen()
    }
}

private fun NavGraphBuilder.showMessages(navController: NavHostController) {
    composable(route = LeafScreen.Messages.route) {
        MessageScreen()
    }
}
