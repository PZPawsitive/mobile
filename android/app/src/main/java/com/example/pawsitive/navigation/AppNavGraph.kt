package com.example.pawsitive.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = RootScreen.Home.route) {
        addHomeRoute(navController)
        addFollowRoute(navController)
        addPetRoute(navController)
        addMessagesRoute(navController)
        addSearchRoute(navController)
        addFavoritesRoute(navController)
        addProfileRoute(navController)
    }
    
}

private fun NavGraphBuilder.addHomeRoute(navController: NavController) {
    navigation(
        route = RootScreen.Home.route,
        startDestination = LeafScreen.Home.route
    ) {
        showHome(navController)
        showHomeDetail(navController)
    }
}
private fun NavGraphBuilder.addFollowRoute(navController: NavController) {
    navigation(
        route = RootScreen.Follow.route,
        startDestination = LeafScreen.Follow.route
    ) {
        showFollow(navController)
    }
}

private fun NavGraphBuilder.addPetRoute(navController: NavController) {
    navigation(
        route = RootScreen.Pet.route,
        startDestination = LeafScreen.Pet.route
    ) {
        showPet(navController)
    }
}

private fun NavGraphBuilder.addMessagesRoute(navController: NavController) {
    navigation(
        route = RootScreen.Messages.route,
        startDestination = LeafScreen.Messages.route
    ) {
        showMessages(navController)
    }
}
private fun NavGraphBuilder.showHome(navController: NavController) {
    composable(route = LeafScreen.Home.route) {
        HomeScreen(
            showDetail = {
                navController.navigate(LeafScreen.HomeDetail.route)
            }
        )
    }
}
private fun NavGraphBuilder.showHomeDetail(navController: NavController) {
    composable(route = LeafScreen.HomeDetail.route) {
        HomeDetailScreen(
            onBack = {
                navController.navigateUp()
            }
        )
    }
}

private fun NavGraphBuilder.showFollow(navController: NavController) {
    composable(route = LeafScreen.Follow.route) {
        FollowScreen()
    }
}

private fun NavGraphBuilder.showPet(navController: NavController) {
    composable(route = LeafScreen.Pet.route) {
        PetScreen()
    }
}

private fun NavGraphBuilder.showMessages(navController: NavController) {
    composable(route = LeafScreen.Messages.route) {
        MessagesScreen()
    }
}

private fun NavGraphBuilder.addSearchRoute(navController: NavController) {
    navigation(
        route = RootScreen.Search.route,
        startDestination = LeafScreen.Search.route
    ) {
        showSearch(navController)
    }
}
private fun NavGraphBuilder.showSearch(navController: NavController) {
    composable(route = LeafScreen.Search.route) {
//        SearchScreen()
    }
}
//end of search navigation

//favorites navigation
private fun NavGraphBuilder.addFavoritesRoute(navController: NavController) {
    navigation(
        route = RootScreen.Favorites.route,
        startDestination = LeafScreen.Favorites.route
    ) {
        showFavorites(navController)
    }
}
private fun NavGraphBuilder.showFavorites(navController: NavController) {
    composable(route = LeafScreen.Favorites.route) {
//        FavoritesScreen()
    }
}
//end of favorites navigation

//profile navigation
private fun NavGraphBuilder.addProfileRoute(navController: NavController) {
    navigation(
        route = RootScreen.Profile.route,
        startDestination = LeafScreen.Profile.route
    ) {
        showProfile(navController)
    }
}
private fun NavGraphBuilder.showProfile(navController: NavController) {
    composable(route = LeafScreen.Profile.route) {
        ProfileScreen()
    }
}