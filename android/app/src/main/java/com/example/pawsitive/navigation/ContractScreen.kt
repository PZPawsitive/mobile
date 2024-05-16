package com.example.pawsitive.navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pawsitive.view.WalkActivity

import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTFrameHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.osmdroid.util.GeoPoint


val exampleContract = Contract("kacper", 15.6, 3, true, GeoPoint(52.237049, 21.017532))

@Composable
fun ContractScreen() {
    WalkNotActiveView()
}

@Composable
fun WalkNotActiveView() {
    val openAlertDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            val location =
                Uri.parse("geo:${exampleContract.localization.latitude},${exampleContract.localization.longitude}?q=${exampleContract.localization.latitude},${exampleContract.localization.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            val chooser = Intent.createChooser(mapIntent, "choose map")

            Text(text = "Contract", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Owner: ${exampleContract.owner}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,  style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Pet quantity: ${exampleContract.petNumber}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,  style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Payment: ${exampleContract.price}$", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,  style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(10.dp))

            Row(Modifier.fillMaxWidth().clickable {
                try {
                    context.startActivity(chooser)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        context,
                        "Cannot find application to handle maps",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }, horizontalArrangement = Arrangement.Center) {
                Text(text = "Show location", style = MaterialTheme.typography.headlineSmall) //
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "open map with localization", Modifier.size(30.dp, 30.dp).align(Alignment.CenterVertically))
            }
            Spacer(modifier = Modifier.height(10.dp))

            when {
                openAlertDialog.value -> {
                    AlertDialog(
                        onDismissRequest = { openAlertDialog.value = false },
                        confirmButton = {
                            Button(onClick = {
                                openAlertDialog.value = false
                                val intent = Intent(context, WalkActivity::class.java)
                                context.startActivity(intent)
                            }) {
                                Text(text = "Tak")
                            }
                        },
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
        FloatingActionButton(onClick = { openAlertDialog.value = !openAlertDialog.value }, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 15.dp)) {
            Box(modifier = Modifier.padding(15.dp)) {
                Text(text = "Accept contract")
            }
        }

    }
}