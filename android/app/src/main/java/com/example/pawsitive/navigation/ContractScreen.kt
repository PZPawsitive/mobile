package com.example.pawsitive.navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

val exampleContract = Contract("kacper", 15.6, 3, true, GeoPoint(52.237049, 21.017532))

@Composable
fun ContractScreen() {
    val openAlertDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column {
        Text(text = "Zlecenie")
        Text(text = "Właściciel: ${exampleContract.owner}")
        Text(text = "Ilość psów: ${exampleContract.petNumber}")
        Text(text = "Wynagrodzenie: ${exampleContract.price}$")
        Row {
            Text(text = "Sprawdź lokalizację") //
            Icon(imageVector = Icons.Default.Map, contentDescription = "open map with localization")
        }
        val location =
            Uri.parse("geo:${exampleContract.localization.latitude},${exampleContract.localization.longitude}?q=${exampleContract.localization.latitude},${exampleContract.localization.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, location)
        val chooser = Intent.createChooser(mapIntent, "choose map")
//        openAlertDialog.value = true
        Button(onClick = {
            try {
                context.startActivity(chooser)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Cannot find application to handle maps", Toast.LENGTH_LONG)
                    .show()
            }
        }) {
            Text(text = "Rozpocznij spacer")
        }
        when {
            openAlertDialog.value -> {
                AlertDialog(
                    onDismissRequest = { openAlertDialog.value = false },
                    confirmButton = { /*TODO*/ },
                    dismissButton = {
                        Button(onClick = { openAlertDialog.value = false }) {
                            Text(text = "Zamknij")
                        }
                    },
                    text = {
                        Map()
                    }
                )
            }
        }

    }
}

@Composable
fun Map() {
    AndroidView(
        modifier = Modifier.padding(10.dp),
        factory = { it ->
            val mapView = MapView(it)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            val mapController = mapView.controller
            mapController.setZoom(17)


            val items = ArrayList<OverlayItem>()
            items.add(
                OverlayItem(
                    exampleContract.owner,
                    "Miejsce docelowe",
                    exampleContract.localization
                )
            )
            mapController.setCenter(items[0].point)
            val overlay = ItemizedOverlayWithFocus<OverlayItem>(items, object :
                ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                    return false
                }
            }, it)

            val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(it), mapView)
            mLocationOverlay.enableMyLocation()
            Log.d("mylocation", mLocationOverlay.myLocation.toString())
//            val line = Polyline()
//            line.width = 4f
//            val points = mutableListOf(
//                GeoPoint(items[0].point.latitude, items[0].point.longitude),
//                GeoPoint(mLocationOverlay.myLocation.latitude, mLocationOverlay.myLocation.longitude)
//            )
//            line.setPoints(points)
//            mapView.overlays.add(line)
            overlay.setFocusItemsOnTap(true);
            mapView.overlays.add(overlay)
            mapView.overlays.add(mLocationOverlay)

            mapView
        }
    )
}