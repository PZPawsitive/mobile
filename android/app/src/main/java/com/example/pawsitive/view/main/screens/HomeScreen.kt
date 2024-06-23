package com.example.pawsitive.view.main.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pawsitive.R
import com.example.pawsitive.models.SimpleGeopoint
import com.example.pawsitive.models.UserDTO
import com.example.pawsitive.navigation.main.MainLeafScreen
import com.example.pawsitive.viewmodel.ApiViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.List
import compose.icons.fontawesomeicons.solid.Map
import kotlinx.coroutines.runBlocking
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    apiViewModel: ApiViewModel,
    updateLocation: () -> Unit,
    getLocation: () -> List<Double>
) {
    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            DogWalkersScreen(navController, apiViewModel, updateLocation, getLocation)
        }
    }
}
@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun DogWalkersScreen(
    navController: NavController,
    apiViewModel: ApiViewModel,
    updateLocation: () -> Unit,
    getLocation: () -> List<Double>
) {
    updateLocation()
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

    val _dogWalkers = remember {
        mutableStateListOf<UserDTO>()
    }
    val dogWalkers: List<UserDTO?> = _dogWalkers

    var range by rememberSaveable {
        mutableStateOf(0.0)
    }
    LaunchedEffect(Unit) {
        val call: Call<List<UserDTO>> = apiViewModel.userService.getDogWalkers()
        call.enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(
                p0: Call<List<UserDTO>>,
                p1: Response<List<UserDTO>>
            ) {
                Log.d("retrofit", p1.body().toString())
                if (p1.body() != null) {
                    _dogWalkers.clear()
                    p1.body()!!.forEach {
                        _dogWalkers.add(it)
                    }
                } else {
                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                p0: Call<List<UserDTO>>,
                p1: Throwable
            ) {
                Log.d("retrofit", p1.message.toString())
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
            }

        })

    }

    val openAlertDialog = remember { mutableStateOf(false) }
    val openSendMessageDialog = remember { mutableStateOf(false) }

    var selectedDogWalker by remember {
        mutableStateOf<String?>(null)
    }

    Box(Modifier.fillMaxSize()) {
        if (viewMode) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    items(items = dogWalkers) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            onClick = {
                                navController.navigate(MainLeafScreen.Chat.route)
                            }
                        ) {
                            Box(modifier = Modifier.padding(start = 10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) { //horizontalArrangement = Arrangement.SpaceBetween,
                                    AsyncImage(
                                        model = it!!.profilePic,
                                        contentDescription = "user profile picture",
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(shape = RoundedCornerShape(40.dp))
                                    )
                                    Column(
                                        Modifier.weight(1f),
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row {
                                            Text(
                                                text = it.firstName,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = it.lastName,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                        Text(text = it.description)
                                    }


                                }
                            }
                        }
                    }
                }
            }

        } else {
            Column() {
                AndroidView(
                    // todo update somehow
                    factory = { it ->
                        val mapView = MapView(it)
                        mapView.setTileSource(TileSourceFactory.MAPNIK)
                        mapView.setMultiTouchControls(true)
                        mapView.zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)

                        val personIcon: Drawable =
                            context.getDrawable(R.drawable.ic_person_find_job)!!
                        val scaledPersonIcon: Drawable = scaleDrawable(personIcon, 15)
                        scaledPersonIcon.colorFilter = PorterDuffColorFilter(
                            context.getColor(R.color.black),
                            PorterDuff.Mode.SRC_IN
                        )
                        val items = ArrayList<OverlayItem>()
                        dogWalkers.forEach { walker ->
                            val overlayItem = OverlayItem(
                                walker!!.id.toString(),
                                walker.firstName,
                                walker.description,
                                GeoPoint(walker.location.latitude, walker.location.longitude)
                            )
                            overlayItem.setMarker(scaledPersonIcon)
                            items.add(overlayItem)
                        }

                        val mapController = mapView.controller
                        val mLocationOverlay =
                            MyLocationNewOverlay(GpsMyLocationProvider(it), mapView)
                        mLocationOverlay.enableMyLocation()
                        mLocationOverlay.enableFollowLocation() // check


                        mapController.setCenter(mLocationOverlay.myLocation)
                        mapController.setZoom(15)
                        mapView.overlays.add(mLocationOverlay)
                        val overlay = ItemizedOverlayWithFocus<OverlayItem>(items, object :
                            ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {

                            override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                                Log.d("osmdroid", item.uid.toString())
                                return true
                            }

                            override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                                Log.d("osmdroid", "long press")
                                selectedDogWalker = item.uid.toString()
                                openSendMessageDialog.value = true
                                return false
                            }
                        }, it)

                        overlay.setFocusItemsOnTap(true)

                        if (range != 0.0) {
                            val myLocation = getLocation()
                            val circle = Polygon.pointsAsCircle(
                                GeoPoint(myLocation[0], myLocation[1]),
                                range * 1000
                            )
                            val polygon = Polygon()
                            polygon.points = circle
                            mapView.overlays.add(polygon)
                        }


                        mapView.overlays.add(overlay)


                        mapView
                    }
                )
            }

        }
        FloatingActionButton(
            onClick = { openAlertDialog.value = !openAlertDialog.value }, modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 15.dp, start = 15.dp)
        ) {
            Box(modifier = Modifier.padding(15.dp)) {
                Icon(
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = "filter by range icon",
                    Modifier.size(25.dp)
                )
            }
        }
        FloatingActionButton(
            onClick = { viewMode = !viewMode },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 15.dp, end = 15.dp)
        ) {
            Icon(
                imageVector = if (viewMode) FontAwesomeIcons.Solid.Map else FontAwesomeIcons.Solid.List,
                contentDescription = "viewmode",
                Modifier.size(25.dp)
            )
        }
        when {
            openAlertDialog.value -> {
                var input by rememberSaveable {
                    mutableStateOf("")
                }
                AlertDialog(
                    modifier = Modifier.width(200.dp),
                    onDismissRequest = { openAlertDialog.value = false },
                    confirmButton = {
                        Button(onClick = {
                            openAlertDialog.value = false
                            val list = getLocation()
                            Log.d("retrofit", list.toString())
                            runBlocking {
                                val call: Call<List<UserDTO>> =
                                    apiViewModel.userService.getDogWalkersNearby(
                                        input.toDouble(), SimpleGeopoint(
                                            list[0], list[1], LocalDateTime.now()
                                        )
                                    )
                                call.enqueue(object : Callback<List<UserDTO>> {
                                    override fun onResponse(
                                        p0: Call<List<UserDTO>>,
                                        p1: Response<List<UserDTO>>
                                    ) {
                                        Log.d("dogwalkers", p1.body().toString())
                                        _dogWalkers.clear()
                                        p1.body()!!.forEach {
                                            _dogWalkers.add(it)
                                        }
                                        range = input.toDouble()
                                    }

                                    override fun onFailure(
                                        p0: Call<List<UserDTO>>,
                                        p1: Throwable
                                    ) {
                                        Log.d("retrofit", p1.message.toString())
                                        Toast.makeText(
                                            context,
                                            "Connection error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                })
                            }

                        }) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "accept")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { openAlertDialog.value = false }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "decline")
                        }
                    },
                    text = {
                        OutlinedTextField(
                            modifier = Modifier.width(150.dp),
                            label = { Text(text = "Kilometres") },
                            value = input,
                            onValueChange = {
                                input = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                )
            }
        }
    }
}
