package com.example.pawsitive.view.walk.screens

import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pawsitive.view.main.MainActivity
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.example.pawsitive.navigation.walk.WalkLeafScreen
import com.example.pawsitive.navigation.walk.WalkNavGraph
import com.example.pawsitive.navigation.walk.WalkRootScreen
import com.example.pawsitive.viewmodel.ApiViewModel
import com.minew.beaconplus.sdk.MTPeripheral


@Composable
fun OverlayWalk(
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit,
    apiViewModel: ApiViewModel,
    registerNavigateAction: (() -> Unit) -> Unit,
) {
    val navController = rememberNavController()
    val currentSelectedScreen by navController.currentScreenAsState()
    val currentRoute by navController.currentRouteAsState()

    fun navigateToDeviceConnected() {
        navController.navigate(WalkLeafScreen.DeviceConnected.route)
    }

    LaunchedEffect(Unit) {
        registerNavigateAction { navigateToDeviceConnected() }
    }

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
    currentSelectedScreen: WalkRootScreen,
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentSelectedScreen == WalkRootScreen.Info,
            onClick = { navController.navigateToRootScreen(WalkRootScreen.Info) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Devices")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == WalkRootScreen.Info) Icons.Default.Info else Icons.Outlined.Info,
                    contentDescription = "devices"
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == WalkRootScreen.Map,
            onClick = { navController.navigateToRootScreen(WalkRootScreen.Map) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Map")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == WalkRootScreen.Map) Icons.Default.Map else Icons.Outlined.Map,
                    contentDescription = "map"
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == WalkRootScreen.Messages,
            onClick = { navController.navigateToRootScreen(WalkRootScreen.Messages) },
            alwaysShowLabel = true,
            label = {
                Text(text = "Messages")
            },
            icon = {
                Icon(
                    imageVector = if (currentSelectedScreen == WalkRootScreen.Messages) Icons.Default.Chat else Icons.AutoMirrored.Outlined.Chat,
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

    val context = LocalContext.current
    CenterAlignedTopAppBar(
        title = { Text(text = "Pawsitive", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { context.startActivity(Intent(context, MainActivity::class.java)) }) {
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
private fun NavController.currentScreenAsState(): State<WalkRootScreen> {
    val selectedItem = remember { mutableStateOf<WalkRootScreen>(WalkRootScreen.Info) }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == WalkRootScreen.Info.route } -> {
                    selectedItem.value = WalkRootScreen.Info
                }

                destination.hierarchy.any { it.route == WalkRootScreen.Map.route } -> {
                    selectedItem.value = WalkRootScreen.Map
                }

                destination.hierarchy.any { it.route == WalkRootScreen.Messages.route } -> {
                    selectedItem.value = WalkRootScreen.Messages
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

private fun NavController.navigateToRootScreen(rootScreen: WalkRootScreen) {
    navigate(rootScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}