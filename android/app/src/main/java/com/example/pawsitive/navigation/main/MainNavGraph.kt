package com.example.pawsitive.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.pawsitive.view.main.screens.ChatScreen
import com.example.pawsitive.view.main.screens.ContractScreen
import com.example.pawsitive.view.main.screens.FollowScreen
import com.example.pawsitive.view.main.screens.HistoryMap
import com.example.pawsitive.view.main.screens.HomeScreen
import com.example.pawsitive.view.main.screens.MessagesScreen
import com.example.pawsitive.view.main.screens.PetAddForm
import com.example.pawsitive.view.main.screens.PetHistoryScreen
import com.example.pawsitive.view.main.screens.PetInfoScreen
import com.example.pawsitive.view.main.screens.PetScreen
import com.example.pawsitive.view.main.screens.ProfileScreen
import com.example.pawsitive.view.main.screens.Settings

@Composable
fun MainNavGraph(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = MainRootScreen.Home.route) {
        addHomeRoute(navController)
        addFollowRoute(navController)
        addPetRoute(navController)
        addMessagesRoute(navController)
        addProfileRoute(navController)
        addSettingsRoute(navController)
    }
    
}

private fun NavGraphBuilder.addHomeRoute(navController: NavController) {
    navigation(
        route = MainRootScreen.Home.route,
        startDestination = MainLeafScreen.Home.route
    ) {
        showHome(navController)
    }
}
private fun NavGraphBuilder.addFollowRoute(navController: NavHostController) {
    navigation(
        route = MainRootScreen.Follow.route,
        startDestination = MainLeafScreen.Follow.route
    ) {
        showFollow(navController)
    }
}

private fun NavGraphBuilder.addPetRoute(
    navController: NavController
) {
    navigation(
        route = MainRootScreen.Pet.route,
        startDestination = MainLeafScreen.Pet.route
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
        route = MainRootScreen.Messages.route,
        startDestination = MainLeafScreen.Messages.route
    ) {
        showMessages(navController)
        showChat(navController)
    }
}
private fun NavGraphBuilder.addProfileRoute(navController: NavController) {
    navigation(
        route = MainRootScreen.Profile.route,
        startDestination = MainLeafScreen.Profile.route
    ) {
        showProfile(navController)
    }
}

private fun NavGraphBuilder.addSettingsRoute(navController: NavController) {
    navigation(route = MainRootScreen.Settings.route, startDestination = MainLeafScreen.Settings.route) {
        showSettings(navController)
    }

}
private fun NavGraphBuilder.showSettings(navController: NavController) {
    composable(route = MainLeafScreen.Settings.route) {
        Settings(navController)
    }
}
private fun NavGraphBuilder.showHome(navController: NavController) {
    composable(route = MainLeafScreen.Home.route) {
        HomeScreen(navController)
    }
}
private fun NavGraphBuilder.showFollow(navController: NavHostController) {
    composable(route = MainLeafScreen.Follow.route) {
        FollowScreen()
    }
}
private fun NavGraphBuilder.showPet(navController: NavController) {
    composable(route = MainLeafScreen.Pet.route) {
        PetScreen(
            navController
        )
    }
}

private fun NavGraphBuilder.showPetInfo(navController: NavController) {
    composable(route = MainLeafScreen.PetInfo.route) {
        PetInfoScreen()
    }
}

private fun NavGraphBuilder.showPetHistory(navController: NavController) {
    composable(route = MainLeafScreen.PetHistory.route) {
        PetHistoryScreen(
            showHistoryMap = {
                navController.navigate(MainLeafScreen.PetHistoryMap.route)
            }
        )
    }
}
private fun NavGraphBuilder.showPetHistoryMap(navController: NavController) {
    composable(route = MainLeafScreen.PetHistoryMap.route) {
        HistoryMap()
    }
}
private fun NavGraphBuilder.showPetAddForm(navController: NavController) {
    composable(route = MainLeafScreen.PetAddForm.route) {
        PetAddForm(navController)
    }
}
private fun NavGraphBuilder.showContractScreen(
    navController: NavController
) {
    composable(route = MainLeafScreen.ContractScreen.route) {
        ContractScreen()
    }
}
private fun NavGraphBuilder.showMessages(navController: NavController) {
    composable(route = MainLeafScreen.Messages.route) {
        MessagesScreen(navController)
    }
}

private fun NavGraphBuilder.showChat(navController: NavController) {
    composable(route = MainLeafScreen.Chat.route) {
        ChatScreen()
    }
}

private fun NavGraphBuilder.showProfile(navController: NavController) {
    composable(route = MainLeafScreen.Profile.route) {
        ProfileScreen()
    }
}