package com.example.pawsitive.view.walk.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.example.pawsitive.navigation.walk.WalkLeafScreen
import com.minew.beaconplus.sdk.MTPeripheral
import com.minew.beaconplus.sdk.enums.FrameType
import com.minew.beaconplus.sdk.exception.MTException
import com.minew.beaconplus.sdk.frames.TlmFrame
import kotlinx.coroutines.delay


@Composable
fun DeviceConnectedScreen(
    beaconViewModel: BeaconViewModel,
    navController: NavHostController,
    disconnect: (MTPeripheral) -> Unit
) {

    val connectedMTPeripheral = beaconViewModel.connectedMTPeripheral
    val connectionHandler = beaconViewModel.connectedMTPeripheral?.mMTConnectionHandler


    fun saveTrigger() {
        val tlmFrame = TlmFrame()
        tlmFrame.frameType = FrameType.FrameTLM

        tlmFrame.temperature = 30.0
        tlmFrame.advInterval = 4000
        tlmFrame.advtxPower = 0
        tlmFrame.radiotxPower = 0


        connectionHandler?.writeSlotFrame(tlmFrame, 5) {
            success, exception ->
            if (success) {
                Log.d("slots", "Slot write success")
            } else {
                Log.d("slots", exception.message)
            }
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = beaconViewModel.connectedMTPeripheral.toString())
            Button(onClick = { saveTrigger() }) {
                Text(text = "set trigger")
            }
            Button(onClick = {
                val noneFrame = TlmFrame()
                noneFrame.frameType = FrameType.FrameNone
                connectionHandler?.writeSlotFrame(noneFrame, 5) {
                        success, exception ->
                    if (success) {
                        Log.d("slots", "Slot write success")
                    } else {
                        Log.d("fail", exception.message)
                    }
                }
                connectionHandler?.resetFactorySetting { success: Boolean, exception: MTException ->
                    if (success) {
                        Log.d("slots", "reset success")
                    }
                    else {
                        Log.d("fail", exception.toString())
                    }
                }
            }) {
                Text(text = "remove from listened")
            }
        }
        FloatingActionButton(
            onClick = {
                navController.navigate(WalkLeafScreen.Info.route)
                if (connectedMTPeripheral != null) {
                    disconnect(connectedMTPeripheral)
                }
            }, modifier = Modifier
                .align(
                    Alignment.BottomCenter
                )
                .padding(bottom = 10.dp)
        ) {
            Text(text = "Back do devices list", modifier = Modifier.padding(10.dp))
        }
    }
}