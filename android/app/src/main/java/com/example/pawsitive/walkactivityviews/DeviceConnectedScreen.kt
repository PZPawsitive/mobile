package com.example.pawsitive.walkactivityviews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pawsitive.viewmodel.BeaconViewModel

@Composable
fun DeviceConnectedScreen(beaconViewModel: BeaconViewModel, navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(text = beaconViewModel.connectedMTPeripheral.toString())
        }
        FloatingActionButton(onClick = { navController.navigate(LeafScreen.Info.route) }, modifier = Modifier.align(
            Alignment.BottomCenter).padding(bottom = 10.dp)) {
            Text(text = "Back do devices list", modifier = Modifier.padding(10.dp))
        }
    }


}