package com.example.pawsitive.view.walk.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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

    val context = LocalContext.current

    IntervalAction {
        Toast.makeText(context, "Wykonywanie akcji co 15 sekund", Toast.LENGTH_SHORT).show()
    }

    fun saveBroadcastParams() {
        val supportedSlots =
            connectedMTPeripheral?.mMTConnectionHandler?.mTConnectionFeature?.supportedSlots;
        val minewFrame = connectedMTPeripheral?.mMTConnectionHandler?.allFrames?.get(2);
        Log.d("beaconplus", minewFrame.toString())
        val frameType = minewFrame?.frameType;
        if (supportedSlots != null) {
            if (!supportedSlots.contains(frameType)) {
                return;
            }
        }
        val curSlot = minewFrame?.curSlot;
        minewFrame?.setAdvInterval(1000);
        minewFrame?.setAdvtxPower(4);
        minewFrame?.setRadiotxPower(-4);
        val smth = connectedMTPeripheral?.mMTConnectionHandler?.mTConnectionFeature?.data
        Log.d("beaconplus", smth.toString())
        connectedMTPeripheral?.mMTConnectionHandler?.writeSlotFrame(minewFrame, curSlot!!) { b, _ ->
            Log.e("beaconplus", "writeSlotFrame success $b")

        }
    }

    fun removeFromListened() {
        if (connectedMTPeripheral != null) {
            beaconViewModel.removeListenedDevice(connectedMTPeripheral)
        }
    }

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

//        val mCurSlot = 2;//需要配置通道的值，2 代表第三通道
//        val version = connectionHandler?.mTConnectionFeature?.version;
//        Log.d("beaconplus", "version: ${version?.value}")
//        if (version?.value!! >= 4) {
//            if (connectionHandler.mTConnectionFeature?.supportTriggers?.size!! > 0
//                && connectionHandler.triggers?.size!! > 0
//            ) {
//                val trigger = Trigger();
//
//                trigger.curSlot = mCurSlot;//选择设置那个通道
//                val isOpen = true; //代表是否开启了触发器
//                if (isOpen) {
//                    val triggerType = TriggerType.BTN_DTAP_EVT;
//                    trigger.triggerType = TriggerType.BTN_DTAP_EVT;//双击按键
////                    connectionHandler.
//                    when (triggerType) {
//                        TriggerType.TEMPERATURE_ABOVE_ALARM -> {
//                            Log.d("beaconplus", "temperature")
//                            trigger.setCondition(10);
//                        }
//
//                        TriggerType.MOTION_DETECT -> TODO()
//                        TriggerType.TEMPERATURE_BELOW_ALARM -> {
//                            TODO()
//                        }
//
//                        TriggerType.HUMIDITY_ABOVE_ALRM -> TODO()
//                        TriggerType.HUMIDITY_BELOW_ALRM -> TODO()
//                        TriggerType.LIGHT_ABOVE_ALRM -> TODO()
//                        TriggerType.BTN_PUSH_EVT -> Log.d("beaconplus", "trigger?")
//                        TriggerType.BTN_RELEASE_EVT -> Log.d("beaconplus", "trigger?")
//                        TriggerType.BTN_STAP_EVT -> {
//                            Log.d("beaconplus", "trigger?")
//                            trigger.condition = 1
//                        }
//
//                        TriggerType.BTN_DTAP_EVT -> Log.d("beaconplus", "trigger?")
//                        TriggerType.BTN_TTAP_EVT -> Log.d("beaconplus", "trigger?")
//                        TriggerType.LIGHT_BELOW_ALARM -> TODO()
//                        TriggerType.FORCE_ABOVE_ALRM -> TODO()
//                        TriggerType.FORCE_BELOW_ALRM -> TODO()
//                        TriggerType.PIR_DETECT -> TODO()
//                        TriggerType.TVOC_ABOVE_ALARM -> TODO()
//                        TriggerType.TVOC_BELOW_ALARM -> {
//                            trigger.setCondition(10);
//                        }
//
//                        TriggerType.VIBRATION_DETECT -> TODO()
//                        TriggerType.LEAKAGE_ALARM -> TODO()
//                        TriggerType.TRIGGER_SRC_NONE -> TODO()
//                        TriggerType.MOTION_DETECT -> TODO()
//                        else -> {
//                            trigger.condition = 10 * 1000;
//                        }
//                    }
//
//                } else {
//                    trigger.triggerType =
//                        TriggerType.TRIGGER_SRC_NONE;//设置是否开启触发器广播。当值为TRIGGER_SRC_NONE时关闭，其它值时开启。
//                    trigger.condition = 10;
//                }
//                val supportedTxpowers = connectionHandler.mTConnectionFeature.supportedTxpowers
//                supportedTxpowers.forEach {
//                    Log.d("beaconplus", it.toString())
//                }
//
//                if (version.value > 4) {
//                    trigger.advInterval = 2000;//广播间隔 100 ms ~ 5000 ms
//                    trigger.radioTxpower = 0;//广播功率：-40dBm ~ 4dBm
////                    trigger.isAlwaysAdvertising = true;//设置是否开启基础参数广播。true：开启，false:关闭。
//                }
////                val temp: TlmFrame = connectedMTPeripheral?.mMTFrameHandler?.advFrames?.get(2) as TlmFrame
//
////                Log.d("beaconplus", "${temp.temperature}")
//                trigger.isAlwaysAdvertising = true
//                connectionHandler.setTriggerCondition(trigger) { b, _ ->
//                    Log.d("beaconplus", "set trigger success $b")
////                    if (connectedMTPeripheral != null) {
////                        beaconViewModel.addListenedDevice(connectedMTPeripheral)
////                    }
//                };
//            }
//        }
//        Log.d("beaconplus", connectionHandler.triggers.toString())
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(text = beaconViewModel.connectedMTPeripheral.toString())
            Button(onClick = { saveBroadcastParams() }) {
                Text(text = "save broadcast params")
            }
            Button(onClick = { saveTrigger() }) {
                Text(text = "set trigger")
            }
            Button(onClick = {
//                removeFromListened()
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

@Composable
fun IntervalAction(everySeconds: Int = 15, action: () -> Unit) {
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(everySeconds * 1000L)
            action()
        }
    }
}