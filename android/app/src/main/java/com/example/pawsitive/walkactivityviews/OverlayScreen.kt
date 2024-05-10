package com.example.pawsitive.walkactivityviews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTPeripheral


@Composable
fun OverlayScreen(
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit
) {
    val navController = rememberNavController()
    val currentSelectedScreen by navController.currentScreenAsState()
    val currentRoute by navController.currentRouteAsState()

    Scaffold(
        topBar = { TopNavBar(navController = navController) },
        bottomBar = {
            BottomAppBar(
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
            WalkNavGraph(navController = navController, beaconViewModel, refresh, connect, disconnect)
        }
    }
}

@Composable
fun BottomAppBar(
    navController: NavHostController,
    currentSelectedScreen: RootScreen
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Info,
            onClick = { navController.navigateToRootScreen(RootScreen.Info) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Info")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == RootScreen.Info) Icons.Default.Info else Icons.Outlined.Info,
                    contentDescription = "info"
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Map,
            onClick = { navController.navigateToRootScreen(RootScreen.Map) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Map")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == RootScreen.Map) Icons.Default.Map else Icons.Outlined.Map,
                    contentDescription = "map"
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
                    imageVector = if (currentSelectedScreen == RootScreen.Messages) Icons.Default.Chat else Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = "messages",
                    modifier = Modifier.size(25.dp)
                )
            }
        )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(navController: NavHostController) {

    var expandedSettings by remember {
        mutableStateOf(false)
    }
    CenterAlignedTopAppBar(
        title = { Text(text = "Pawsitive", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { expandedSettings = !expandedSettings }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }

            DropdownMenu(
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
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Info) }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == RootScreen.Info.route } -> {
                    selectedItem.value = RootScreen.Info
                }

                destination.hierarchy.any { it.route == RootScreen.Map.route } -> {
                    selectedItem.value = RootScreen.Map
                }

                destination.hierarchy.any { it.route == RootScreen.Messages.route } -> {
                    selectedItem.value = RootScreen.Messages
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