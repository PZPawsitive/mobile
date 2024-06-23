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
import com.example.pawsitive.view.main.screens.ContractAddForm
import com.example.pawsitive.view.main.screens.ContractListScreen
import com.example.pawsitive.view.main.screens.ContractScreen
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
import kotlin.reflect.KFunction0

@Composable
fun MainNavGraph(
    navController: NavHostController,
    apiViewModel: ApiViewModel,
    updateLocation: () -> Unit,
    getLocation: KFunction0<List<Double>>,
) {
    NavHost(navController = navController, startDestination = MainRootScreen.Home.route) {
        addHomeRoute(navController, apiViewModel, updateLocation,getLocation)
        addContractListRoute(navController, apiViewModel)
        addPetRoute(navController, apiViewModel)
        addMessagesRoute(navController, apiViewModel)
        addProfileRoute(navController, apiViewModel)
        addSettingsRoute(navController)
    }
    
}

private fun NavGraphBuilder.addHomeRoute(
    navController: NavController,
    apiViewModel: ApiViewModel,
    updateLocation: () -> Unit,
    getLocation: () -> List<Double>
) {
    navigation(
        route = MainRootScreen.Home.route,
        startDestination = MainLeafScreen.Home.route
    ) {
        showHome(navController, apiViewModel, updateLocation,getLocation)
    }
}
private fun NavGraphBuilder.addContractListRoute(
    navController: NavHostController,
    apiViewModel: ApiViewModel
) {
    navigation(
        route = MainRootScreen.ContractList.route,
        startDestination = MainLeafScreen.ContractList.route
    ) {
        showContractList(navController, apiViewModel)
        showContractAddForm(navController, apiViewModel)
        showContractScreen(navController, apiViewModel)
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
private fun NavGraphBuilder.showHome(
    navController: NavController,
    apiViewModel: ApiViewModel,
    updateLocation: () -> Unit,
    getLocation: () -> List<Double>
) {
    composable(route = MainLeafScreen.Home.route) {
        HomeScreen(navController, apiViewModel, updateLocation,getLocation)
    }
}
private fun NavGraphBuilder.showContractList(
    navController: NavHostController,
    apiViewModel: ApiViewModel
) {
    composable(route = MainLeafScreen.ContractList.route) {
        ContractListScreen(navController, apiViewModel)
    }
}
private fun NavGraphBuilder.showContractAddForm(
    navController: NavController,
    apiViewModel: ApiViewModel
) {
    composable(route = MainLeafScreen.ContractAddForm.route) {
        ContractAddForm(navController, apiViewModel)
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
    composable(route = "${MainLeafScreen.PetHistoryMap.route}?id={id}", arguments = listOf(
        navArgument("id") {}
    )) {backStackEntry ->
        HistoryMap(apiViewModel, backStackEntry.arguments?.getString("id"))
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
        ContractScreen(apiViewModel, backStackEntry.arguments?.getString("id"), navController)
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