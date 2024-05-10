package com.example.pawsitive.walkactivityviews

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTFrameHandler
import com.minew.beaconplus.sdk.MTPeripheral
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoScreen(
    beaconViewModel: BeaconViewModel,
    refresh: () -> Unit,
    connect: (MTPeripheral) -> Unit,
    disconnect: (MTPeripheral) -> Unit
) {
    var connected by remember {
        mutableStateOf(false)
    }
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {

        Column {
            Text(
                text = if (!connected) "Podłącz się do urządzenia" else "Spacer aktywny",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            PeripheralList(beaconViewModel = beaconViewModel, connect, disconnect)
        }
        FloatingActionButton(
            onClick = { onRefresh() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .size(width = 100.dp, 50.dp)
        ) {
            Text(text = "Refresh")
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
    disconnect: (MTPeripheral) -> Unit
) {
    val mlist = beaconViewModel.mlist
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = mlist) {
            val mtFrameHandler: MTFrameHandler = it.mMTFrameHandler
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onClick = {
                    if (mtFrameHandler.name == "D15N") {
                        connect(it)
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
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "disconnect", Modifier.clickable { disconnect(it) })
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
    }
}