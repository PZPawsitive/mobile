package com.example.pawsitive.walkactivityviews

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.pawsitive.viewmodel.BeaconViewModel

@Composable
fun DeviceConnectedScreen(beaconViewModel: BeaconViewModel) {
    Text(text = beaconViewModel.connectedMTPeripheral.toString())
}