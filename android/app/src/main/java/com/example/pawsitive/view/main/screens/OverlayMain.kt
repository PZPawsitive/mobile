package com.example.pawsitive.view.main.screens

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.pawsitive.navigation.main.MainNavGraph
import com.example.pawsitive.navigation.main.MainRootScreen
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.view.auth.LoginActivity
import com.example.pawsitive.viewmodel.ApiViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Paw
import kotlin.reflect.KFunction0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlayMain(
    apiViewModel: ApiViewModel,
    updateLocation: () -> Unit,
    getLocation: KFunction0<List<Double>>
) {
    val navController = rememberNavController()
    val currentSelectedScreen by navController.currentScreenAsState()
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    //    val currentRoute by navController.currentRouteAsState()

    var expandedSettings by remember {
        mutableStateOf(false)
    }
    var state by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Pawsitive", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cofnij się"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expandedSettings = !expandedSettings }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Otwórz menu"
                        )
                    }

                    DropdownMenu(
                        expanded = expandedSettings,
                        onDismissRequest = { expandedSettings = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Ustawienia") },
                            onClick = {
                                navController.navigate(MainRootScreen.Settings.route)
                                expandedSettings = false
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(text = { Text(text = "Wyloguj") }, onClick = {
                            preferencesManager.clear()
                            context.startActivity(
                                Intent(context, LoginActivity::class.java)
                            )
                        })
                    }
                }
            )

        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentSelectedScreen = currentSelectedScreen
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            MainNavGraph(
                navController = navController,
                apiViewModel,
                updateLocation,
                getLocation
            )
        }
    }
}


@Composable
private fun BottomNavBar(
    navController: NavHostController,
    currentSelectedScreen: MainRootScreen
) {

    NavigationBar {
        NavigationBarItem(
            selected = currentSelectedScreen == MainRootScreen.Home,
            onClick = { navController.navigateToRootScreen(MainRootScreen.Home) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Główna")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == MainRootScreen.Home) Icons.Default.Home else Icons.Outlined.Home,
                    contentDescription = "strona główna"
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == MainRootScreen.ContractList,
            onClick = { navController.navigateToRootScreen(MainRootScreen.ContractList) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Zlecenia")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == MainRootScreen.ContractList) Icons.Filled.List else Icons.Outlined.List,
                    contentDescription = "ZLecenia"
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == MainRootScreen.Pet,
            onClick = { navController.navigateToRootScreen(MainRootScreen.Pet) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Zwierzaki")
            },
            icon = {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Paw,
                    contentDescription = "Zwierzaki",
                    modifier = Modifier.size(25.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == MainRootScreen.Messages,
            onClick = { navController.navigateToRootScreen(MainRootScreen.Messages) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Czat")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == MainRootScreen.Messages) Icons.AutoMirrored.Filled.Chat else Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = "Wiadomości"
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == MainRootScreen.Profile,
            onClick = { navController.navigateToRootScreen(MainRootScreen.Profile) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Profil")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == MainRootScreen.Profile) Icons.Filled.ManageAccounts else Icons.Outlined.ManageAccounts,
                    contentDescription = "Profil"
                )
            }
        )

    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<MainRootScreen> {
    val selectedItem = remember { mutableStateOf<MainRootScreen>(MainRootScreen.Home) }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == MainRootScreen.Home.route } -> {
                    selectedItem.value = MainRootScreen.Home
                }

                destination.hierarchy.any { it.route == MainRootScreen.ContractList.route } -> {
                    selectedItem.value = MainRootScreen.ContractList
                }

                destination.hierarchy.any { it.route == MainRootScreen.Pet.route } -> {
                    selectedItem.value = MainRootScreen.Pet
                }

                destination.hierarchy.any { it.route == MainRootScreen.Messages.route } -> {
                    selectedItem.value = MainRootScreen.Messages
                }

                destination.hierarchy.any { it.route == MainRootScreen.Profile.route } -> {
                    selectedItem.value = MainRootScreen.Profile
                }

                destination.hierarchy.any { it.route == MainRootScreen.Settings.route } -> {
                    selectedItem.value = MainRootScreen.Settings
                }
            }

        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}

@Stable
@Composable
private fun NavController.currentRouteAsState(): State<String?> {
    val selectedItem = remember { mutableStateOf<String?>(null) }
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem.value = destination.route
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}

private fun NavController.navigateToRootScreen(rootScreen: MainRootScreen) {
    navigate(rootScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}