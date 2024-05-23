package com.example.pawsitive.view.walk.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pawsitive.view.main.MainActivity
import com.example.pawsitive.view.walk.WalkActivity
import com.example.pawsitive.viewmodel.ApiViewModel
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTFrameHandler
import com.minew.beaconplus.sdk.MTPeripheral
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                text = "Devices nearby",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            PeripheralList(beaconViewModel = beaconViewModel, connect, disconnect)
            if (beaconViewModel.listenedDevices.isNotEmpty()) {
                Text(
                    text = "Followed devices",
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
                Text(text = "Walk Completed")
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
                                            Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    override fun onFailure(
                                        p0: Call<String>,
                                        p1: Throwable
                                    ) {
                                        Log.d("retrofit", p1.message.toString())
                                        Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
                                    }

                                })
                            }
                        }) {
                            Text(text = "Yes")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { openAlertDialog.value = false }) {
                            Text(text = "No")
                        }
                    },
                    text = {
                        Text(text = "Are you at paymaster's location?")
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
            Device(connect = connect, disconnect = disconnect, mtPeripheral = it)
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
            Device(connect = connect, disconnect = disconnect, mtPeripheral = it)
        }
    }
}

@Composable
fun Device(
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit,
    mtPeripheral: MTPeripheral
) {
    val context = LocalContext.current
    val mtFrameHandler: MTFrameHandler = mtPeripheral.mMTFrameHandler
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),

        onClick = {
            if (mtFrameHandler.name == "D15N") {
                connect(mtPeripheral)
            } else {
                Toast.makeText(context, "wait for device to configure", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    ) {
        Box(modifier = Modifier.padding(10.dp)) {
            if (mtFrameHandler.name == "D15N") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = mtFrameHandler.name)
                    Text(text = "battery: ${mtFrameHandler.battery}%")
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "disconnect",
                        Modifier.clickable { disconnect(mtPeripheral) })
                }
            } else {

                Text(
                    text = "Poczekaj aż się skonfiguruje",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }

    }
}

