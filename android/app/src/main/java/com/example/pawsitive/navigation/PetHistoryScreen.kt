package com.example.pawsitive.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.osmdroid.util.GeoPoint
import java.util.Date

data class History(val description: String, val date: Date, val geopoints: List<GeoPoint>)

val histories = listOf(
    History("super spacer", Date(), listOf(GeoPoint(1, 1), GeoPoint(2, 2))),
    History("lipny spacer", Date(), listOf(GeoPoint(1, 1))),
    History("walka psów", Date(), listOf(GeoPoint(1, 1))),
    History("super droga", Date(), listOf(GeoPoint(1, 1))),
    History("piesek był zadowolony", Date(), listOf(GeoPoint(1, 1))),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetHistoryScreen(showHistoryMap: () -> Unit) {
    Column {
        Text(text = "Historia spacerów")
    }
    LazyColumn(modifier = Modifier.padding(10.dp)) {
        items(items = histories) {
            var expandedSettings by remember {
                mutableStateOf(false)
            }
            Box() {
                DropdownMenu( // ui broken - fix
                    expanded = expandedSettings,
                    onDismissRequest = { expandedSettings = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Zmień opis") },
                        onClick = { TODO() })
                    Divider()
                    DropdownMenuItem(text = { Text(text = "Usuń spacer") }, onClick = { TODO() })
                    Divider()
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