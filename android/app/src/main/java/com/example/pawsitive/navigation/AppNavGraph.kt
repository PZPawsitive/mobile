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
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = RootScreen.Home.route) {
        addHomeRoute(navController)
        addFollowRoute(navController)
        addPetRoute(navController)
        addMessagesRoute(navController)
        addProfileRoute(navController)
    }
    
}

private fun NavGraphBuilder.addHomeRoute(navController: NavController) {
    navigation(
        route = RootScreen.Home.route,
        startDestination = LeafScreen.Home.route
    ) {
        showHome(navController)
    }
}
private fun NavGraphBuilder.addFollowRoute(navController: NavHostController) {
    navigation(
        route = RootScreen.Follow.route,
        startDestination = LeafScreen.Follow.route
    ) {
        showFollow(navController)
    }
}

private fun NavGraphBuilder.addPetRoute(
    navController: NavController
) {
    navigation(
        route = RootScreen.Pet.route,
        startDestination = LeafScreen.Pet.route
    ) {
        showPet(navController)
        showPetInfo(navController)
        showPetHistory(navController)
        showPetHistoryMap(navController)
        showPetAddForm(navController)
        showContractScreen(navController)
    }
}

private fun NavGraphBuilder.addMessagesRoute(navController: NavController) {
    navigation(
        route = RootScreen.Messages.route,
        startDestination = LeafScreen.Messages.route
    ) {
        showMessages(navController)
        showChat(navController)
    }
}
private fun NavGraphBuilder.addProfileRoute(navController: NavController) {
    navigation(
        route = RootScreen.Profile.route,
        startDestination = LeafScreen.Profile.route
    ) {
        showProfile(navController)
    }
}
private fun NavGraphBuilder.showHome(navController: NavController) {
    composable(route = LeafScreen.Home.route) {
        HomeScreen()
    }
}
private fun NavGraphBuilder.showFollow(navController: NavHostController) {
    composable(route = LeafScreen.Follow.route) {
        FollowScreen()
    }
}
private fun NavGraphBuilder.showPet(navController: NavController) {
    composable(route = LeafScreen.Pet.route) {
        PetScreen(
            navController
        )
    }
}

private fun NavGraphBuilder.showPetInfo(navController: NavController) {
    composable(route = LeafScreen.PetInfo.route) {
        PetInfoScreen()
    }
}

private fun NavGraphBuilder.showPetHistory(navController: NavController) {
    composable(route = LeafScreen.PetHistory.route) {
        PetHistoryScreen(
            showHistoryMap = {
                navController.navigate(LeafScreen.PetHistoryMap.route)
            }
        )
    }
}
private fun NavGraphBuilder.showPetHistoryMap(navController: NavController) {
    composable(route = LeafScreen.PetHistoryMap.route) {
        HistoryMap()
    }
}
private fun NavGraphBuilder.showPetAddForm(navController: NavController) {
    composable(route = LeafScreen.PetAddForm.route) {
        PetAddForm()
    }
}
private fun NavGraphBuilder.showContractScreen(
    navController: NavController
) {
    composable(route = LeafScreen.ContractScreen.route) {
        ContractScreen()
    }
}
private fun NavGraphBuilder.showMessages(navController: NavController) {
    composable(route = LeafScreen.Messages.route) {
        MessagesScreen(navController)
    }
}

private fun NavGraphBuilder.showChat(navController: NavController) {
    composable(route = LeafScreen.Chat.route) {
        ChatScreen()
    }
}

private fun NavGraphBuilder.showProfile(navController: NavController) {
    composable(route = LeafScreen.Profile.route) {
        ProfileScreen()
    }
}