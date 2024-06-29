package com.example.pawsitive.view.walk.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pawsitive.navigation.walk.WalkLeafScreen
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTPeripheral
import com.minew.beaconplus.sdk.enums.FrameType
import com.minew.beaconplus.sdk.exception.MTException
import com.minew.beaconplus.sdk.frames.TlmFrame
import com.minew.beaconplus.sdk.frames.UidFrame


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
        tlmFrame.advtxPower = -59
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


    if (connectedMTPeripheral == null) {
        navController.navigate(WalkLeafScreen.Info.route)
    }

    var nameSpaceInput by rememberSaveable {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(value = nameSpaceInput, onValueChange = {nameSpaceInput = it}, placeholder = { Text(
                text = "Imię zwierzęcia"
            )})
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(onClick = {
                val uidFrame = UidFrame()
                uidFrame.frameType = FrameType.FrameUID

                uidFrame.namespaceId = nameSpaceInput
                uidFrame.instanceId = nameSpaceInput;

                uidFrame.setRadiotxPower(4)
                uidFrame.setAdvInterval(600)
                uidFrame.setAdvtxPower(-59)
                connectionHandler?.writeSlotFrame(uidFrame, 1) {
                        success, exception ->
                    if (success) {
                        Log.d("slots", success.toString())
//                        Toast.makeText(context, "Saved name!", Toast.LENGTH_SHORT).show() // sdk bug
                    } else {
                        Log.d("slots", exception.message)
//                        Toast.makeText(context, "Could not save name", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text(text = "Zapisz imię")
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(onClick = { saveTrigger() }) {
                Text(text = "Nasłuchuj urządzenia")
            }
            Spacer(modifier = Modifier.height(10.dp))
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
                Text(text = "Usuń z nasłuchiwanych")
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
            Text(text = "Powrót do urządzeń", modifier = Modifier.padding(10.dp))
        }
    }
}