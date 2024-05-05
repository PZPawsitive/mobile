package com.example.pawsitive.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


data class Post(val owner: String, val content: String)

val posts = listOf(
    Post("kacper", "szukam wyprowadzacza psów w warszawie"),
    Post("melchior", "szukam wyprowadzacza psów w gdanski"),
    Post("baltazar", "szukam wyprowadzacza psów w sopocie"),
    Post("radek", "szukam wyprowadzacza psów w warszawie")
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    showDetail: () -> Unit
) {

    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            if (LocalGlobalState.current) {
                PostsScreen()
            } else {
                DogWalkersScreen()
            }
//            Text(
//                modifier = Modifier.align(Alignment.Center),
//                text = "Home Screen", style = MaterialTheme.typography.headlineMedium
//            )
//
//            Button(
//                modifier = Modifier
//                    .padding(top = 100.dp)
//                    .align(Alignment.Center),
//                onClick = {
//                    showDetail()
//                }
//            ) { Text(text = "go home details") }
//            AndroidView(
//                modifier = Modifier.fillMaxSize(),
//                factory = {
//                    val mapView = MapView(it)
//                    mapView.setTileSource(TileSourceFactory.MAPNIK)
//                    mapView.setBuiltInZoomControls(true)
//                    mapView.setMultiTouchControls(true)
//                    mapView
//                }
//                // CHECKOUT BRANCH
//            )
        }
    }
}

@Composable
fun PostsScreen() {
    LazyColumn {
        items(items = posts) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Green)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = it.owner, fontWeight = FontWeight.Bold)
                    Text(text = it.content)
                }
            }
        }
    }
}


data class DogWalker(val name: String, val description: String ,val geoPoint: GeoPoint)

val dogWalkers = listOf(
    DogWalker("kacper", "dogwalker",GeoPoint(52.370816, 9.735936))
)

@Composable
fun DogWalkersScreen() {
    var viewMode by remember {
        mutableStateOf(true)
    }
    Column(
        modifier = Modifier.border(2.dp, Color.Blue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewMode = true }, enabled = !viewMode) {
                Text(text = "List")
            }
            Button(onClick = { viewMode = false }, enabled = viewMode) {
                Text(text = "Map")
            }

        }
        if (viewMode) {
            LazyColumn {
                items(items = dogWalkers) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(text = it.name)
                        Text(text = "${it.geoPoint}")
                    }
                }
            }
        } else {
            Column(modifier = Modifier.padding(top = 40.dp).border(2.dp, color = Color.Green)) {
                AndroidView(
                    modifier = Modifier.padding(10.dp).border(2.dp, Color.Red),
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
                            override fun onItemSingleTapUp(index:Int, item:OverlayItem):Boolean {
                                return true
                            }
                            override fun onItemLongPress(index:Int, item:OverlayItem):Boolean {
                                return false
                            }
                        }, it)
                        overlay.setFocusItemsOnTap(true);
                        mapView.overlays.add(overlay)

                        mapView
                    }
                    // CHECKOUT BRANCH
                )
            }

        }
    }
}