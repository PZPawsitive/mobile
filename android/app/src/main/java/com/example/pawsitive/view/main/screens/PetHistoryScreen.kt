package com.example.pawsitive.view.main.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pawsitive.viewmodel.ApiViewModel
import org.osmdroid.util.GeoPoint
import java.util.Date

data class History(val description: String, val date: Date, val geopoints: List<GeoPoint>)

val histories = mutableStateListOf(
    History("super spacer", Date(), listOf(GeoPoint(1, 1), GeoPoint(2, 2))),
    History("lipny spacer", Date(), listOf(GeoPoint(1, 1))),
    History("walka psów", Date(), listOf(GeoPoint(1, 1))),
    History("super droga", Date(), listOf(GeoPoint(1, 1))),
    History("piesek był zadowolony", Date(), listOf(GeoPoint(1, 1))),
)

@SuppressLint("UnrememberedMutableState")
@Composable
fun PetHistoryScreen(showHistoryMap: () -> Unit, apiViewModel: ApiViewModel) {
    val openAlertDialog = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            Text(
                text = "Historia spacerów",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )


            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                items(items = histories) {
                    var expandedSettings by remember {
                        mutableStateOf(false)
                    }
                    Box {
                        DropdownMenu( // ui broken - fix
                            expanded = expandedSettings,
                            onDismissRequest = { expandedSettings = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Zmień opis") },
                                onClick = {
                                    expandedSettings = !expandedSettings
                                    openAlertDialog.value = !openAlertDialog.value
                                })
                            HorizontalDivider()
                            DropdownMenuItem(text = { Text(text = "Usuń spacer") }, onClick = {
                                expandedSettings = !expandedSettings
                                histories.remove(it)
                            })
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text(text = "Zobacz na mapie") },
                                onClick = { showHistoryMap() })
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            onClick = { expandedSettings = !expandedSettings }
                        ) {
                            Column(modifier = Modifier.padding(5.dp)) {
                                Text(text = it.description, fontWeight = FontWeight.Bold)
                                Text(text = it.date.toString())
                            }
                        }
                    }

                }
            }
        }
        when {
            openAlertDialog.value -> {
                var input by rememberSaveable {
                    mutableStateOf("")
                }
                AlertDialog(
                    onDismissRequest = { openAlertDialog.value = false },
                    confirmButton = {
                        Button(onClick = {
                            openAlertDialog.value = false
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
                        OutlinedTextField(value = input, onValueChange = {input = it})
                    }
                )
            }
        }
    }


}