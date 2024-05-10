package com.example.pawsitive.navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.pawsitive.viewmodel.BeaconViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

//import androidx.compose.material3.pulltorefresh

val exampleContract = Contract("kacper", 15.6, 3, true, GeoPoint(52.237049, 21.017532))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractScreen(beaconViewModel: BeaconViewModel) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val pullToRefreshState = rememberPullToRefreshState()

    fun onRefresh() {
        Log.d("refresh", "on refresh")
    }

    var walkMode by remember {
        mutableStateOf<Boolean>(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        Column {
            val location =
            Uri.parse("geo:${exampleContract.localization.latitude},${exampleContract.localization.longitude}?q=${exampleContract.localization.latitude},${exampleContract.localization.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            val chooser = Intent.createChooser(mapIntent, "choose map")
            Text(text = "Zlecenie")
            Text(text = "Właściciel: ${exampleContract.owner}")
            Text(text = "Ilość psów: ${exampleContract.petNumber}")
            Text(text = "Wynagrodzenie: ${exampleContract.price}$")
            Row {
                Text(text = "Sprawdź lokalizację") //
                Icon(imageVector = Icons.Default.Map, contentDescription = "open map with localization", Modifier.clickable {
                    try {
                        context.startActivity(chooser)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "Cannot find application to handle maps", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }

            Button(onClick = {
                openAlertDialog.value = true
            }) {
                Text(text = "Rozpocznij spacer")
            }
            PeripheralList(beaconViewModel)
            when {
                openAlertDialog.value -> {
                    AlertDialog(
                        onDismissRequest = { openAlertDialog.value = false },
                        confirmButton = { Button(onClick = {
                            openAlertDialog.value = false
                            walkMode = true
                        }) {
                            Text(text = "Tak")
                        } },
                        dismissButton = {
                            Button(onClick = { openAlertDialog.value = false }) {
                                Text(text = "Nie")
                            }
                        },
                        text = {
                            Text(text = "Czy chcesz rozpocząć spacer?")
                        }
                    )
                }
            }

        }
        var isRefreshing by remember {
            mutableStateOf<Boolean>(false)
        }
        Button(onClick = { isRefreshing = !isRefreshing }) {
            Text(text = "refresh")
        }
        if(pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(isRefreshing) {
            if(isRefreshing) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }
        PullToRefreshContainer(state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter))
    }
}


@Composable
fun PeripheralList(
    beaconViewModel: BeaconViewModel
) {
    val mlist = beaconViewModel.mlist
//    Log.d("beaconData","mlist: ${mlist.toList()}")

    LazyColumn(
        modifier = Modifier.height(50.dp)
    ) {
        items(items = mlist) {
            Card {
                Text(text = it.toString())
            }
        }
    }


}