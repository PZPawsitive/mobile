package com.example.pawsitive.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Paw


var LocalGlobalState = compositionLocalOf<Boolean> { error("not composed") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentSelectedScreen by navController.currentScreenAsState()
    val currentRoute by navController.currentRouteAsState()

    var expandedSettings by remember {
        mutableStateOf(false)
    }



    var state by remember {
        mutableStateOf(false)
    }

    CompositionLocalProvider(LocalGlobalState provides state) {


        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Pawsitive", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { TODO() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        Text(text = "Walker", modifier = Modifier.padding(end = 5.dp))
                        Switch(checked = state, onCheckedChange = { state = !state })
                        IconButton(onClick = { expandedSettings = !expandedSettings }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }

                        DropdownMenu( // ui broken - fix
                            expanded = expandedSettings,
                            onDismissRequest = { expandedSettings = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Settings") },
                                onClick = { /*TODO*/ })
                            Divider()
                            DropdownMenuItem(text = { Text(text = "...") }, onClick = { /*TODO*/ })
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
                AppNavGraph(navController = navController)
            }
        }

    }
}


@Composable
private fun BottomNavBar(
    navController: NavController,
    currentSelectedScreen: RootScreen
) {

    NavigationBar {
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Home,
            onClick = { navController.navigateToRootScreen(RootScreen.Home) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Home")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == RootScreen.Home) Icons.Default.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Follow,
            onClick = { navController.navigateToRootScreen(RootScreen.Follow) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Follow")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == RootScreen.Follow) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Follows"
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Pet,
            onClick = { navController.navigateToRootScreen(RootScreen.Pet) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Pet")
            },
            icon = {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Paw,
                    contentDescription = "Pets",
                    modifier = Modifier.size(25.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Messages,
            onClick = { navController.navigateToRootScreen(RootScreen.Messages) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Messages")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == RootScreen.Messages) Icons.Default.Chat else Icons.Outlined.Chat,
                    contentDescription = "home"
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Profile,
            onClick = { navController.navigateToRootScreen(RootScreen.Profile) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Profile")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == RootScreen.Profile) Icons.Filled.ManageAccounts else Icons.Outlined.ManageAccounts,
                    contentDescription = "home"
                )
            }
        )

    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Home) }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == RootScreen.Home.route } -> {
                    selectedItem.value = RootScreen.Home
                }

                destination.hierarchy.any { it.route == RootScreen.Follow.route } -> {
                    selectedItem.value = RootScreen.Follow
                }

                destination.hierarchy.any { it.route == RootScreen.Pet.route } -> {
                    selectedItem.value = RootScreen.Pet
                }

                destination.hierarchy.any { it.route == RootScreen.Messages.route } -> {
                    selectedItem.value = RootScreen.Messages
                }

                destination.hierarchy.any { it.route == RootScreen.Profile.route } -> {
                    selectedItem.value = RootScreen.Profile
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

private fun NavController.navigateToRootScreen(rootScreen: RootScreen) {
    navigate(rootScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}