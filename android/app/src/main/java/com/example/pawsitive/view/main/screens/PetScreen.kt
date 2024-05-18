package com.example.pawsitive.view.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.pawsitive.ui.theme.Purple40
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cat
import compose.icons.fontawesomeicons.solid.Dog
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.pawsitive.navigation.main.MainLeafScreen
import org.osmdroid.util.GeoPoint

enum class PetType {
    DOG,
    CAT
}

data class Pet(val name: String, val type: PetType)

val pets =
    listOf(
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
    )

data class Contract(
    val owner: String,
    val price: Double,
    val petNumber: Number,
    val danger: Boolean,
    val localization: GeoPoint
)

val contracts = listOf(
    Contract("kacper", 15.6, 3, true, GeoPoint(1, 1)),
    Contract("kacper", 15.6, 3, false, GeoPoint(1, 1)),
    Contract("kacper", 15.6, 3, false, GeoPoint(1, 1)),
    Contract("kacper", 15.6, 3, true, GeoPoint(1, 1))
)

@Composable
fun PetScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (LocalGlobalState.current) {
            Contracts(
                navController)
        } else {
            MyPetsColumn(
                navController
            )
        }
    }


}

@Composable
fun MyPetsColumn(
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn() {
            items(items = pets) {
                var expandedSettings by remember {
                    mutableStateOf(false)
                }
                Box() {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        onClick = {
                            expandedSettings = !expandedSettings
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = it.name)
                            Icon(
                                imageVector = if (it.type == PetType.DOG) FontAwesomeIcons.Solid.Dog else FontAwesomeIcons.Solid.Cat,
                                contentDescription = "pet type",
                                Modifier.size(30.dp)
                            )
                        }
                        DropdownMenu(
                            // ui broken - fix
                            expanded = expandedSettings,
                            onDismissRequest = { expandedSettings = false },
                        ) {

                            DropdownMenuItem(
                                text = { Text(text = "Info") },
                                onClick = { navController.navigate(MainLeafScreen.PetInfo.route) })
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text(text = "Historia spacerów") },
                                onClick = { navController.navigate(MainLeafScreen.PetHistory.route) })
                        }
                    }

                }

            }

        }
        FloatingActionButton(
            onClick = { navController.navigate(MainLeafScreen.PetAddForm.route) },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add my pet")
        }
    }

}


@Composable
fun Contracts(
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.padding(10.dp)
    ) {
        items(items = contracts) {
            var expandedSettings by remember {
                mutableStateOf(false)
            }
            Box() {
                Card(
                    onClick = { expandedSettings = !expandedSettings },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = if (it.danger) CardDefaults.cardColors(containerColor = Color.Red) else CardDefaults.cardColors(
                        containerColor = Purple40
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = it.owner, fontWeight = FontWeight.Bold)
                        Text(text = "${it.petNumber} pets")
                        Text(text = "${it.price} $", fontWeight = FontWeight.Bold)
                    }
                }
                DropdownMenu(
                    // ui broken - fix
                    expanded = expandedSettings,
                    onDismissRequest = { expandedSettings = false },
                ) {

                    DropdownMenuItem(
                        text = { Text(text = "Napisz wiadomość") },
                        onClick = { navController.navigate(MainLeafScreen.Chat.route) })
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text(text = "Pokaż szczegóły") },
                        onClick = { navController.navigate(MainLeafScreen.ContractScreen.route) })
                }
            }

        }
    }
}
