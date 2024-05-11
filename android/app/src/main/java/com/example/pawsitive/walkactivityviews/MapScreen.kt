package com.example.pawsitive.walkactivityviews

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.pawsitive.navigation.exampleHistory
import com.example.pawsitive.viewmodel.BeaconViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapScreen() {
    AndroidView(
        modifier = Modifier.padding(10.dp),
        factory = {
            val mapView = MapView(it)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            val mapController = mapView.controller
            mapController.setZoom(17)


//            mapController.setCenter(items[0].point)

            val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(it), mapView)
            mLocationOverlay.enableMyLocation()
            mapView.overlays.add(mLocationOverlay)

            // test if works?
            mLocationOverlay.enableFollowLocation()


            mapController.setCenter(mLocationOverlay.myLocation)
//            val line = Polyline()
//            line.width = 4f
//            line.setPoints(items.map { GeoPoint(it.point.latitude, it.point.longitude) })
//            mapView.overlays.add(line)

            mapView.overlays.add(mLocationOverlay)

            mapView
        }
    )
}