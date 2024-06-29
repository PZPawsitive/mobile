package com.example.pawsitive.view.main.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.pawsitive.models.Geopoint
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
import java.time.format.DateTimeFormatter

@Composable
fun HistoryMap(apiViewModel: ApiViewModel, contractId: String?) {
    Log.d("retrofit", "contract id ${contractId.toString()}")
    val context = LocalContext.current
    var geopoints: List<Geopoint>? by remember {
        mutableStateOf(null)
    }
    runBlocking {
        val call: Call<List<Geopoint>> =
            apiViewModel.walkService.getGeopoints(contractId.toString())
        call.enqueue(object : Callback<List<Geopoint>> {
            override fun onResponse(
                p0: Call<List<Geopoint>>,
                p1: Response<List<Geopoint>>
            ) {
                Log.d("retrofit", p1.body().toString())
                if (p1.body() != null) {

                    geopoints = p1.body()!!
                } else {
                    Toast.makeText(context, "Błąd, spróbuj ponownie", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                p0: Call<List<Geopoint>>,
                p1: Throwable
            ) {
                Log.d("retrofit", p1.message.toString())
                Toast.makeText(context, "Błąd połączenia", Toast.LENGTH_SHORT).show()
            }

        })
    }
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
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                geopoints!!.forEach {
                    items.add(
                        OverlayItem(
                            it.createdAt.format(formatter),
                            "Punkt trasy",
                            GeoPoint(it.latitude, it.longitude)
                        )
                    )
                    items.add(
                        OverlayItem(
                            it.createdAt.format(formatter),
                            "Punkt trasy",
                            GeoPoint(it.latitude + 0.0001, it.longitude + 0.0001)
                        )
                    )
                }
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
                overlay.setFocusItemsOnTap(true)
                mapView.overlays.add(overlay)

                val line = Polyline()
                line.width = 4f
                line.setPoints(items.map { GeoPoint(it.point.latitude, it.point.longitude) })
                mapView.overlays.add(line)


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
                
                mapView
            }
        )
    }

}