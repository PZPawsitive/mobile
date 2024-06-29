package com.example.pawsitive.view.walk.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pawsitive.view.main.MainActivity
import com.example.pawsitive.viewmodel.ApiViewModel
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTFrameHandler
import com.minew.beaconplus.sdk.MTPeripheral
import com.minew.beaconplus.sdk.enums.FrameType
import com.minew.beaconplus.sdk.frames.UidFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoScreen(
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit,
    navController: NavHostController,
    apiViewModel: ApiViewModel,
    historyId: String?
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()


    var isRefreshing by remember {
        mutableStateOf<Boolean>(false)
    }

    fun onRefresh() {
        scope.launch {
            refresh()
            isRefreshing = true
            delay(3000L)
//            beaconViewModel.setBeaconList(emptyList())
            isRefreshing = false
        }
    }
    val openAlertDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)

    ) {

        Column {
            Text(
                text = "Urządzenia w pobliżu",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            PeripheralList(beaconViewModel = beaconViewModel, connect, disconnect)
            if (beaconViewModel.listenedDevices.isNotEmpty()) {
                Text(
                    text = "Nasłuchiwane urządzenia",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                ListenedDevicesList(
                    beaconViewModel = beaconViewModel,
                    connect = connect,
                    disconnect = disconnect
                )
            }
        }
        FloatingActionButton(onClick = { openAlertDialog.value = !openAlertDialog.value }, modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 15.dp)) {
            Box(modifier = Modifier.padding(15.dp)) {
                Text(text = "Zakończ spacer")
            }
        }
        when {
            openAlertDialog.value -> {
                AlertDialog(
                    onDismissRequest = { openAlertDialog.value = false },
                    confirmButton = {
                        Button(onClick = {
                            runBlocking {
                                val call: Call<String> = apiViewModel.walkService.completeContract(historyId!!)
                                call.enqueue(object : Callback<String> {
                                    override fun onResponse(
                                        p0: Call<String>,
                                        p1: Response<String>
                                    ) {
                                        if (p1.body() != null) {
                                            openAlertDialog.value = false
                                            val intent = Intent(
                                                context,
                                                MainActivity::class.java
                                            )
                                            context.startActivity(intent)
                                        } else {
                                            Log.d("retrofit", p1.body().toString())
                                            Toast.makeText(context, "Błąd, spróbuj ponownie", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    override fun onFailure(
                                        p0: Call<String>,
                                        p1: Throwable
                                    ) {
                                        Log.d("retrofit", p1.message.toString())
                                        Toast.makeText(context, "Błąd połączenia", Toast.LENGTH_SHORT).show()
                                    }

                                })
                            }
                        }) {
                            Text(text = "Tak")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { openAlertDialog.value = false }) {
                            Text(text = "Nie")
                        }
                    },
                    text = {
                        Text(text = "Jesteś w lokalizacji początkowej?")
                    }
                )
            }
        }
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }
        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun PeripheralList(
    beaconViewModel: BeaconViewModel,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit,
) {
    val mlist = beaconViewModel.mlist
    LazyColumn(
//        modifier = Modifier.fillMaxSize()
    ) {
        items(items = mlist) {
            Device(connect = connect, disconnect = disconnect, mtPeripheral = it, beaconViewModel)
        }
    }
}

@Composable
fun ListenedDevicesList(
    beaconViewModel: BeaconViewModel,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit,
) {
    val listenedDevicesList = beaconViewModel.listenedDevices
    LazyColumn {
        items(items = listenedDevicesList) {
            Device(
                connect = connect,
                disconnect = disconnect,
                mtPeripheral = it,
                beaconViewModel = beaconViewModel
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun Device(
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit,
    mtPeripheral: MTPeripheral,
    beaconViewModel: BeaconViewModel
) {
    val context = LocalContext.current
    val mtFrameHandler: MTFrameHandler = mtPeripheral.mMTFrameHandler

    fun calculateDistance(rssi: Int, measuredPower: Int, n: Double = 4.0):Double {
        return 10.0.pow((measuredPower - rssi) / (10 * n))
    }

    if (beaconViewModel.listenedDevices.contains(mtPeripheral)) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),

            onClick = {
                connect(mtPeripheral)
            }
        ) {
            Box(modifier = Modifier.padding(10.dp)) {
                if (mtFrameHandler.name != null && mtPeripheral.mMTConnectionHandler != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier
                            .background(
                                shape = CircleShape,
                                color = if (mtFrameHandler.name == "D15N") Color.Green else Color.Red
                            ) // sdk bug on isConnectable
                            .padding(5.dp)) {
                        }
                        mtPeripheral.mMTConnectionHandler.allFrames.forEach {
                            Log.d("slots", "${it.curSlot} ${it.frameType} ${it}")
                            if (it.frameType == FrameType.FrameUID) {
                                val frame = it as UidFrame
                                Text(text = frame.namespaceId, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(text = "${String.format("%.2f", calculateDistance(mtFrameHandler.rssi, -59))} m")
                        Text(text = "batteria: ${mtFrameHandler.battery}%")
//                    Log.d("frames", "frametype: ${mtPeripheral.mMTConnectionHandler.mTConnectionFeature} ")
                    }
                } else {

                    Text(
                        text = "Poczekaj na konfigurację urządzenia",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }

        }
    } else {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),

            onClick = {
                if (mtFrameHandler.name == "D15N") {
                    connect(mtPeripheral)
                } else {
                    Toast.makeText(context, "Poczekaj na konfigurację urządzenia", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        ) {
            Box(modifier = Modifier.padding(10.dp)) {
                if (mtFrameHandler.name != null && mtPeripheral.mMTConnectionHandler != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier
                            .background(
                                shape = CircleShape,
                                color = if (mtFrameHandler.name == "D15N") Color.Green else Color.Red
                            ) // sdk bug on isConnectable
                            .padding(5.dp)) {
                        }
                        Text(text = mtFrameHandler.name)
                        Text(text = "${String.format("%.2f", calculateDistance(mtFrameHandler.rssi, -59))} m")
                        Text(text = "batteria: ${mtFrameHandler.battery}%")
                    }
                } else {

                    Text(
                        text = "Poczekaj na konfigurację urządzenia",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }

        }
    }


}

