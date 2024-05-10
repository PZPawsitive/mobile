package com.example.pawsitive.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.pawsitive.viewmodel.BeaconViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    beaconViewModel: BeaconViewModel
) {
    NavHost(navController = navController, startDestination = RootScreen.Home.route) {
        addHomeRoute(navController)
        addFollowRoute(navController, beaconViewModel)
        addPetRoute(navController, beaconViewModel)
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
        showHomeDetail(navController)
    }
}
private fun NavGraphBuilder.addFollowRoute(navController: NavHostController, beaconViewModel: BeaconViewModel) {
    navigation(
        route = RootScreen.Follow.route,
        startDestination = LeafScreen.Follow.route
    ) {
        showFollow(navController, beaconViewModel)
    }
}

private fun NavGraphBuilder.addPetRoute(navController: NavController, beaconViewModel: BeaconViewModel) {
    navigation(
        route = RootScreen.Pet.route,
        startDestination = LeafScreen.Pet.route
    ) {
        showPet(navController)
        showPetInfo(navController)
        showPetHistory(navController)
        showPetHistoryMap(navController)
        showPetAddForm(navController)
        showContractScreen(navController, beaconViewModel)
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
private fun NavGraphBuilder.showFollow(navController: NavHostController, beaconViewModel: BeaconViewModel) {
    composable(route = LeafScreen.Follow.route) {
        FollowScreen(beaconViewModel)
    }
}
private fun NavGraphBuilder.showPet(navController: NavController) {
    composable(route = LeafScreen.Pet.route) {
        PetScreen(
            showPetHistory = {
                navController.navigate(LeafScreen.PetHistory.route)
            },
            showPetInfo = {
                navController.navigate(LeafScreen.PetInfo.route)
            },
            showPetAddForm = {
                navController.navigate(LeafScreen.PetAddForm.route)
            },
            showContractScreen = {
                navController.navigate(LeafScreen.ContractScreen.route)
            }
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
    navController: NavController,
    beaconViewModel: BeaconViewModel
) {
    composable(route = LeafScreen.ContractScreen.route) {
        ContractScreen(beaconViewModel)
    }
}
private fun NavGraphBuilder.showMessages(navController: NavController) {
    composable(route = LeafScreen.Messages.route) {
        MessagesScreen()
    }
}

private fun NavGraphBuilder.showProfile(navController: NavController) {
    composable(route = LeafScreen.Profile.route) {
        ProfileScreen()
    }
}