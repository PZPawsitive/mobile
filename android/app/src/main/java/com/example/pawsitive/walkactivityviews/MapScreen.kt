package com.example.pawsitive.walkactivityviews

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.pawsitive.viewmodel.BeaconViewModel

@Composable
fun MapScreen(beaconViewModel: BeaconViewModel) {
    Column {
        Text(text = "Map screen")
        Text(text = beaconViewModel.connectedMTPeripheral.toString())
    }
}