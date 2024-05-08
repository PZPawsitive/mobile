package com.example.pawsitive.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem

@Composable
fun HistoryMap() {
    AndroidView(
        modifier = Modifier.padding(10.dp),
        factory = {
            val mapView = MapView(it)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)

//                    val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(it), mapView)
//                    mLocationOverlay.enableMyLocation()
//                    mapView.getOverlays().add(mLocationOverlay)

            val items = ArrayList<OverlayItem>()
            items.add(OverlayItem(dogWalkers[0].name, dogWalkers[0].description, dogWalkers[0].geoPoint))
            val overlay = ItemizedOverlayWithFocus<OverlayItem>(items, object:
                ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index:Int, item: OverlayItem):Boolean {
                    return true
                }
                override fun onItemLongPress(index:Int, item: OverlayItem):Boolean {
                    return false
                }
            }, it)
            overlay.setFocusItemsOnTap(true);
            mapView.overlays.add(overlay)

            mapView
        }
    )
}