package com.example.pawsitive.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
import com.example.pawsitive.viewmodel.ApiViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController,
    apiViewModel: ApiViewModel,
) {
    NavHost(navController = navController, startDestination = MainRootScreen.Home.route) {
        addHomeRoute(navController, apiViewModel)
        addFollowRoute(navController)
        addPetRoute(navController, apiViewModel)
        addMessagesRoute(navController, apiViewModel)
        addProfileRoute(navController, apiViewModel)
        addSettingsRoute(navController)
    }
    
}

private fun NavGraphBuilder.addHomeRoute(navController: NavController, apiViewModel: ApiViewModel) {
    navigation(
        route = MainRootScreen.Home.route,
        startDestination = MainLeafScreen.Home.route
    ) {
        showHome(navController, apiViewModel)
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
    navController: NavController,
    apiViewModel: ApiViewModel
) {
    navigation(
        route = MainRootScreen.Pet.route,
        startDestination = MainLeafScreen.Pet.route
    ) {
        showPet(navController, apiViewModel)
        showPetInfo(navController, apiViewModel)
        showPetHistory(navController, apiViewModel)
        showPetHistoryMap(navController, apiViewModel)
        showPetAddForm(navController, apiViewModel)
        showContractScreen(navController, apiViewModel)
    }
}

private fun NavGraphBuilder.addMessagesRoute(
    navController: NavController,
    apiViewModel: ApiViewModel
) {
    navigation(
        route = MainRootScreen.Messages.route,
        startDestination = MainLeafScreen.Messages.route
    ) {
        showMessages(navController, apiViewModel)
        showChat(navController, apiViewModel)
    }
}
private fun NavGraphBuilder.addProfileRoute(
    navController: NavController,
    apiViewModel: ApiViewModel
) {
    navigation(
        route = MainRootScreen.Profile.route,
        startDestination = MainLeafScreen.Profile.route
    ) {
        showProfile(navController, apiViewModel)
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
private fun NavGraphBuilder.showHome(navController: NavController, apiViewModel: ApiViewModel) {
    composable(route = MainLeafScreen.Home.route) {
        HomeScreen(navController, apiViewModel)
    }
}
private fun NavGraphBuilder.showFollow(navController: NavHostController) {
    composable(route = MainLeafScreen.Follow.route) {
        FollowScreen()
    }
}
private fun NavGraphBuilder.showPet(navController: NavController, apiViewModel: ApiViewModel) {
    composable(route = MainLeafScreen.Pet.route) {
        PetScreen(
            navController, apiViewModel
        )
    }
}

private fun NavGraphBuilder.showPetInfo(navController: NavController, apiViewModel: ApiViewModel) {
    composable(route = "${MainLeafScreen.PetInfo.route}?petId={petId}", arguments = listOf(
        navArgument("petId") {})) { backStackEntry ->
        PetInfoScreen(apiViewModel, backStackEntry.arguments?.getString("petId"), navController)
    }
}

private fun NavGraphBuilder.showPetHistory(navController: NavController, apiViewModel: ApiViewModel) {
    composable(route = "${MainLeafScreen.PetHistory.route}?petId={petId}", arguments = listOf(
        navArgument("petId") {}
    )) {backStackEntry ->
        PetHistoryScreen(
            navController,
            apiViewModel,
            backStackEntry.arguments?.getString("petId")
        )
    }
}
private fun NavGraphBuilder.showPetHistoryMap(
    navController: NavController,
    apiViewModel: ApiViewModel
) {
    composable(route = MainLeafScreen.PetHistoryMap.route) {
        HistoryMap(apiViewModel)
    }
}
private fun NavGraphBuilder.showPetAddForm(navController: NavController, apiViewModel: ApiViewModel) {
    composable(route = MainLeafScreen.PetAddForm.route) {
        PetAddForm(navController, apiViewModel)
    }
}
private fun NavGraphBuilder.showContractScreen(
    navController: NavController,
    apiViewModel: ApiViewModel
) {
    composable(route = "${MainLeafScreen.ContractScreen.route}?id={id}",
        arguments = listOf(
            navArgument("id") {}
        )) {backStackEntry ->
        ContractScreen(apiViewModel, backStackEntry.arguments?.getString("id"))
    }
}
private fun NavGraphBuilder.showMessages(navController: NavController, apiViewModel: ApiViewModel) {
    composable(route = MainLeafScreen.Messages.route) {
        MessagesScreen(navController, apiViewModel)
    }
}

private fun NavGraphBuilder.showChat(navController: NavController, apiViewModel: ApiViewModel) {
    composable(route = MainLeafScreen.Chat.route) {
        ChatScreen(apiViewModel)
    }
}

private fun NavGraphBuilder.showProfile(navController: NavController, apiViewModel: ApiViewModel) {
    composable(route = MainLeafScreen.Profile.route) {
        ProfileScreen(apiViewModel)
    }
}