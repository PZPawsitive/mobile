package com.example.pawsitive.view.main.screens

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pawsitive.models.AddPetRequest
import com.example.pawsitive.models.IdWrapper
import com.example.pawsitive.models.User
import com.example.pawsitive.navigation.main.MainLeafScreen
import com.example.pawsitive.util.DateUtils
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.viewmodel.ApiViewModel
import com.minew.beaconplus.sdk.MTPeripheral
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cat
import compose.icons.fontawesomeicons.solid.Dog
import compose.icons.fontawesomeicons.solid.Dove
import compose.icons.fontawesomeicons.solid.Horse
import compose.icons.fontawesomeicons.solid.Paw
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetAddForm(navController: NavController, apiViewModel: ApiViewModel) {
    var nameInput by rememberSaveable {
        mutableStateOf("")
    }
    var showDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val birthDate = rememberDatePickerState()
    val millisToLocalDate = birthDate.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    val dateToString = birthDate.selectedDateMillis?.let {
        DateUtils().dateToString(millisToLocalDate!!)
    } ?: "Choose birthdate"

    var expandedSpecies by rememberSaveable { mutableStateOf(false) }
    var expandedBreed by rememberSaveable { mutableStateOf(false) }

    val species = listOf("DOG", "CAT", "BIRD", "OTHER")
    val dogBreeds = listOf("Labrador", "Bulldog", "Alaskan Malamute", "Other")
    val catBreeds = listOf("Birman", "Bombay", "Bengal", "Other")
    val birdBreeds = listOf("Canary", "Columbidae", "Cockatiel", "Finch", "Lovebird", "Other")
    val otherBreeds = listOf("Other")

    var breeds = mutableListOf("DOG", "CAT", "BIRD", "OTHER")


    var selectedSpecies by rememberSaveable {
        mutableStateOf(species[0])
    }
    var selectedBreed by rememberSaveable {
        mutableStateOf(dogBreeds[0])
    }

    val context = LocalContext.current

    val preferencesManager = PreferencesManager(context)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text(text = "Name") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { showDialog.value = !showDialog.value }) {
                Text(
                    text = dateToString,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "date picker",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), horizontalArrangement = Arrangement.Center
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedSpecies,
                    onExpandedChange = { expandedSpecies = !expandedSpecies },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    TextField(
                        value = selectedSpecies,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSpecies,
                        onDismissRequest = { expandedSpecies = false }) {
                        species.forEach {
                            DropdownMenuItem(text = {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(text = it)
                                    Icon(
                                        imageVector = when (it) {
                                            "DOG" -> FontAwesomeIcons.Solid.Dog
                                            "CAT" -> FontAwesomeIcons.Solid.Cat
                                            "BIRD" -> FontAwesomeIcons.Solid.Dove
                                            "HORSE" -> FontAwesomeIcons.Solid.Horse
                                            else -> FontAwesomeIcons.Solid.Paw
                                        },
                                        contentDescription = "icon",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }, onClick = {
                                selectedSpecies = it
                                expandedSpecies = false
                            })
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            when (selectedSpecies) {
                "DOG" -> {
                    breeds.clear()
                    dogBreeds.forEach { breeds.add(it) }
                    selectedBreed = dogBreeds[0]
                }

                "CAT" -> {
                    breeds.clear()
                    catBreeds.forEach { breeds.add(it) }
                    selectedBreed = catBreeds[0]
                }

                "BIRD" -> {
                    breeds.clear()
                    birdBreeds.forEach { breeds.add(it) }
                    selectedBreed = birdBreeds[0]
                }

                "OTHER" -> {
                    breeds.clear()
                    otherBreeds.forEach { breeds.add(it) }
                    selectedBreed = otherBreeds[0]
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), horizontalArrangement = Arrangement.Center
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedBreed,
                    onExpandedChange = { expandedBreed = !expandedBreed },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    TextField(
                        value = selectedBreed,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedBreed,
                        onDismissRequest = { expandedBreed = false }) {
                        breeds.forEach {
                            DropdownMenuItem(text = {
                                Text(text = it)
                            }, onClick = {
                                selectedBreed = it
                                expandedBreed = false
                            })
                        }

                    }
                }
            }


            when {
                showDialog.value -> {
                    DatePickerDialog(
                        onDismissRequest = { showDialog.value = false },
                        confirmButton = {
                            Button(onClick = {
                                showDialog.value = false

                            }) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "accept"
                                )
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDialog.value = false }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "decline"
                                )
                            }
                        },
                    ) {
                        DatePicker(state = birthDate, showModeToggle = true)
                    }
                }
            }
        }
        if (nameInput.isNotEmpty() && birthDate.selectedDateMillis != null)
            FloatingActionButton(
                onClick = {
                    runBlocking {
                        val userId = preferencesManager.getUserId()
                        val call: Call<String> = apiViewModel.petService.addPet(
                            AddPetRequest(
                                nameInput,
                                selectedSpecies,
                                selectedBreed,
                                millisToLocalDate!!,
                                userId!!
                            )
                        )
                        call.enqueue(object : Callback<String> {
                            override fun onResponse(
                                p0: Call<String>,
                                p1: Response<String>
                            ) {
                                Log.d("retrofit", p1.body().toString())
                                if (p1.body() != null) {
                                    Toast.makeText(
                                        context,
                                        "Succesfully added pet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(MainLeafScreen.Pet.route)
                                } else {
                                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun onFailure(
                                p0: Call<String>,
                                p1: Throwable
                            ) {
                                Log.d("retrofit", p1.message.toString())
                                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        })
                    }

                }, modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 15.dp)
            ) {
                Box(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Add pet")
                }
            }
    }

}