package com.example.pawsitive.navigation

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import com.example.pawsitive.navigation.History
import org.osmdroid.views.overlay.Polyline
import java.util.Date

val exampleHistory = History(
    "super spacer",
    Date(),
    listOf(GeoPoint(52.237049, 21.017532), GeoPoint(53.237049, 22.017532), GeoPoint(53.237049, 23.017532))
)

@Composable
fun HistoryMap() {
    AndroidView(
        modifier = Modifier.padding(10.dp),
        factory = { it ->
            val mapView = MapView(it)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            val mapController = mapView.controller
            mapController.setZoom(17)


//                    val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(it), mapView)
//                    mLocationOverlay.enableMyLocation()
//                    mapView.getOverlays().add(mLocationOverlay)

            val items = ArrayList<OverlayItem>()
            exampleHistory.geopoints.forEach {
                items.add(
                    OverlayItem(
                        exampleHistory.date.toString(),
                        exampleHistory.description,
                        it
                    )
                )
            }
            Log.d("items", items.toString())
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
            val line = Polyline()
            line.width = 4f
            line.setPoints(items.map { GeoPoint(it.point.latitude, it.point.longitude) })
            mapView.overlays.add(line)
            overlay.setFocusItemsOnTap(true);
            mapView.overlays.add(overlay)

            mapView
        }
    )
}