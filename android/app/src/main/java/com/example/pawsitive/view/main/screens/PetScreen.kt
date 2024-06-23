package com.example.pawsitive.view.main.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cat
import compose.icons.fontawesomeicons.solid.Dog
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.pawsitive.models.Pet
import com.example.pawsitive.navigation.main.MainLeafScreen
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.viewmodel.ApiViewModel
import compose.icons.fontawesomeicons.solid.Dove
import compose.icons.fontawesomeicons.solid.Horse
import compose.icons.fontawesomeicons.solid.Paw
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun PetScreen(
    navController: NavController,
    apiViewModel: ApiViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
            MyPetsColumn(
                navController,
                apiViewModel
            )
    }
}

@Composable
fun MyPetsColumn(
    navController: NavController,
    apiViewModel: ApiViewModel
) {
    var pets: List<Pet>? by remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    runBlocking {
        val call: Call<List<Pet>> = apiViewModel.petService.getPetsByUserId(preferencesManager.getUserId()!!)
        call.enqueue(object : Callback<List<Pet>> {
            override fun onResponse(
                p0: Call<List<Pet>>,
                p1: Response<List<Pet>>
            ) {
                Log.d("retrofit", p1.body().toString())
                if (p1.body() != null) {
                    pets = p1.body()
                } else {
                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                p0: Call<List<Pet>>,
                p1: Throwable
            ) {
                Log.d("retrofit", p1.message.toString())
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
            }

        })
    }
    if (pets != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn() {
                items(items = pets!!) {
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
                                    imageVector = when (it.species) {
                                        "DOG" -> FontAwesomeIcons.Solid.Dog
                                        "CAT" -> FontAwesomeIcons.Solid.Cat
                                        "BIRD" -> FontAwesomeIcons.Solid.Dove
                                        "HORSE" -> FontAwesomeIcons.Solid.Horse
                                        else -> FontAwesomeIcons.Solid.Paw
                                    },
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
                                    onClick = { navController.navigate("${MainLeafScreen.PetInfo.route}?petId=${it.id}") })
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text(text = "Historia spacerów") },
                                    onClick = { navController.navigate("${MainLeafScreen.PetHistory.route}?petId=${it.id}") })
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

    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp).align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }

}

