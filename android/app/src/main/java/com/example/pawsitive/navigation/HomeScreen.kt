package com.example.pawsitive.navigation

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.example.pawsitive.R
import compose.icons.AllIcons
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.List
import compose.icons.fontawesomeicons.solid.Map
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
fun HomeScreen() {

    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            if (LocalGlobalState.current) {
                PostsScreen()
            } else {
                DogWalkersScreen()
            }
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


data class DogWalker(val name: String, val description: String, val geoPoint: GeoPoint)

val dogWalkers = listOf(
    DogWalker("kacper", "dogwalker", GeoPoint(52.237049, 21.017532)),
    DogWalker("kacper", "dogwalker", GeoPoint(53.237049, 22.017532)),
    DogWalker("kacper", "dogwalker", GeoPoint(53.237049, 23.017532))
)

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun DogWalkersScreen() {
    var viewMode by remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current

    fun scaleDrawable(drawable: Drawable, dpSize: Int): Drawable {
        val metrics = context.resources.displayMetrics
        val pxSize = (dpSize * metrics.density).toInt()
        val bitmap = (drawable as BitmapDrawable).bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, pxSize, pxSize, true)
        return BitmapDrawable(context.resources, scaledBitmap)
    }

    Box(Modifier.fillMaxSize()) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(onClick = { viewMode = true }, enabled = !viewMode) {
//                Text(text = "List")
//            }
//            Button(onClick = { viewMode = false }, enabled = viewMode) {
//                Text(text = "Map")
//            }
//
//        }
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
            Column() {
                AndroidView(

                    factory = { it ->
                        val mapView = MapView(it)
                        mapView.setTileSource(TileSourceFactory.MAPNIK)
                        mapView.setMultiTouchControls(true)
                        mapView.zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)

                        val personIcon: Drawable = context.getDrawable(R.drawable.ic_person_find_job)!!
                        val scaledPersonIcon: Drawable = scaleDrawable(personIcon, 15)
                        scaledPersonIcon.colorFilter = PorterDuffColorFilter(context.getColor(R.color.black), PorterDuff.Mode.SRC_IN)
                        val items = ArrayList<OverlayItem>()
                        dogWalkers.forEach {walker ->
                            val overlayItem = OverlayItem(walker.name, walker.description, walker.geoPoint)
                            overlayItem.setMarker(scaledPersonIcon)
                            items.add(
                                overlayItem
                            )
                        }
                        val mapController = mapView.controller
                        val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(it), mapView)
                        mLocationOverlay.enableMyLocation()


                        mLocationOverlay.enableFollowLocation() // check
                        mapController.setCenter(mLocationOverlay.myLocation)
                        mapController.setZoom(15)

                        val overlay = ItemizedOverlayWithFocus<OverlayItem>(items, object :
                            ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                            override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                                return true
                            }

                            override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                                return false
                            }
                        }, it)
                        overlay.setFocusItemsOnTap(true);
                        mapView.overlays.add(overlay)
                        mapView.overlays.add(mLocationOverlay)
                        mapView
                    }
                    // CHECKOUT BRANCH
                )
            }

        }
        FloatingActionButton(onClick = { viewMode = !viewMode },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 15.dp, end = 15.dp)) {
            Icon(imageVector = if (viewMode) FontAwesomeIcons.Solid.Map else FontAwesomeIcons.Solid.List, contentDescription = "viewmode", Modifier.size(25.dp))
        }
    }
}
