package com.example.pawsitive.walkactivityviews

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation


@Composable
fun WalkNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = RootScreen.Info.route) {
        addInfoRoute(navController)
        addMapRoute(navController)
        addMessagesRoute(navController)
    }
}

private fun NavGraphBuilder.addInfoRoute(navController: NavHostController) {
    navigation(route = RootScreen.Info.route, startDestination = LeafScreen.Info.route) {
        showInfo(navController)
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

private fun NavGraphBuilder.showInfo(navController: NavHostController) {
    composable(route = LeafScreen.Info.route) {
        InfoScreen()
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
