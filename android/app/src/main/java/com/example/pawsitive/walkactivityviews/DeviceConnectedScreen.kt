package com.example.pawsitive.walkactivityviews

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.pawsitive.viewmodel.BeaconViewModel

@Composable
fun DeviceConnectedScreen(beaconViewModel: BeaconViewModel, navController: NavHostController) {
    Column {
        Text(text = beaconViewModel.connectedMTPeripheral.toString())
        Button(onClick = { navController.navigate(LeafScreen.Info.route) }) {
            Text(text = "wróć do urządzeń")
        }
    }

}