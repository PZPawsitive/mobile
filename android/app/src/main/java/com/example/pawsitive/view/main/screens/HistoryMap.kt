package com.example.pawsitive.view.main.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.pawsitive.models.Contract
import com.example.pawsitive.models.GeopointDTO
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

//val exampleHistory = History(
//    "super spacer",
//    Date(),
//    listOf(GeoPoint(52.237049, 21.017532), GeoPoint(53.237049, 22.017532), GeoPoint(53.237049, 23.017532))
//)

@Composable
fun HistoryMap(apiViewModel: ApiViewModel, contractId: String?) {
    Log.d("retrofit", "kontrakt id ${contractId.toString()}")
    val context = LocalContext.current
    var geopoints: List<GeopointDTO>? by remember {
        mutableStateOf(null)
    }
    runBlocking {
        val call: Call<List<GeopointDTO>> =
            apiViewModel.walkService.getGeopoints(contractId.toString())
        call.enqueue(object : Callback<List<GeopointDTO>> {
            override fun onResponse(
                p0: Call<List<GeopointDTO>>,
                p1: Response<List<GeopointDTO>>
            ) {
                Log.d("retrofit", p1.body().toString())
                if (p1.body() != null) {

                    geopoints = p1.body()!!
                } else {
                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                p0: Call<List<GeopointDTO>>,
                p1: Throwable
            ) {
                Log.d("retrofit", p1.message.toString())
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
            }

        })
    }
    Log.d("raz", geopoints.toString())
    if (geopoints != null && geopoints!!.isNotEmpty()) {
        AndroidView(
            modifier = Modifier.padding(10.dp),
            factory = { it ->
                val mapView = MapView(it)
                mapView.setTileSource(TileSourceFactory.MAPNIK)
                mapView.setMultiTouchControls(true)
                val mapController = mapView.controller
                mapController.setZoom(17)
                val items = ArrayList<OverlayItem>()
                geopoints!!.forEach {
                    items.add(
                        OverlayItem(
                            it.createdAt.toString(),
                            "geopoint",
                            GeoPoint(it.latitude, it.longitude)
                        )
                    )
                }
                Log.d("retrofit", "set")
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
                mapView.overlays.add(overlay)


//                    val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(it), mapView)
//                    mLocationOverlay.enableMyLocation()
//                    mapView.getOverlays().add(mLocationOverlay)
//            mapController.setCenter(items[0].point)
                // my location
//            val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(it), mapView)
//            mLocationOverlay.enableMyLocation()
//            mapView.overlays.add(mLocationOverlay)


                val line = Polyline()
                line.width = 4f
//            line.setPoints(items.map { GeoPoint(it.point.latitude, it.point.longitude) })
                mapView.overlays.add(line)
//            overlay.setFocusItemsOnTap(true);
//            mapView.overlays.add(overlay)

                mapView
            }
        )
    } else {
        AndroidView(
            modifier = Modifier.padding(10.dp),
            factory = { it ->
                val mapView = MapView(it)
                mapView.setTileSource(TileSourceFactory.MAPNIK)
                mapView.setMultiTouchControls(true)
                val mapController = mapView.controller
                mapController.setZoom(17)

                val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(it), mapView)
                mLocationOverlay.enableMyLocation()
                mapView.overlays.add(mLocationOverlay)
                mLocationOverlay.enableFollowLocation()


                mapController.setCenter(mLocationOverlay.myLocation)


//                val line = Polyline()
//                line.width = 4f
//            line.setPoints(items.map { GeoPoint(it.point.latitude, it.point.longitude) })
//                mapView.overlays.add(line)
//            overlay.setFocusItemsOnTap(true);
//            mapView.overlays.add(overlay)

                mapView
            }
        )
    }

}