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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pawsitive.models.Species
import com.example.pawsitive.navigation.main.MainLeafScreen
import com.example.pawsitive.util.DateUtils
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.viewmodel.ApiViewModel
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
    val species = listOf(Species.DOG, Species.CAT, Species.BIRD, Species.OTHER)
    val breeds = mutableListOf("Labrador", "Bulldog", "Samojed", "Inny")
    val dogBreeds = listOf("Labrador", "Bulldog", "Samojed", "Inny")
    val catBreeds = listOf("Syjamski", "Sfinks", "Perski", "Inny")
    val birdBreeds = listOf("Kanarek", "Gołąb", "Nimfa", "Gil", "Zięba", "Inny")
    val otherBreeds = listOf("Inny")

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    val birthDate = rememberDatePickerState()
    val millisToLocalDate = birthDate.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }
    val dateToString = birthDate.selectedDateMillis?.let {
        DateUtils().dateToString(millisToLocalDate!!)
    } ?: "Wybierz datę urodzenia"

    var nameInput by rememberSaveable {
        mutableStateOf("")
    }
    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }
    var expandedSpecies by rememberSaveable { mutableStateOf(false) }
    var expandedBreed by rememberSaveable { mutableStateOf(false) }

    var selectedSpecies by rememberSaveable {
        mutableStateOf(species[0])
    }
    var selectedBreed by rememberSaveable {
        mutableStateOf(dogBreeds[0])
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text(text = "Imię") },
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
                    contentDescription = "Data",
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
                        value = selectedSpecies.translatedName,
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
                                    Text(text = it.translatedName)
                                    Icon(
                                        imageVector = when (it) {
                                            Species.DOG -> FontAwesomeIcons.Solid.Dog
                                            Species.CAT -> FontAwesomeIcons.Solid.Cat
                                            Species.BIRD -> FontAwesomeIcons.Solid.Dove
                                            else -> FontAwesomeIcons.Solid.Paw
                                        },
                                        contentDescription = "ikona zwierzęcia",
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
                Species.DOG -> {
                    breeds.clear()
                    dogBreeds.forEach { breeds.add(it) }
                    selectedBreed = dogBreeds[0]
                }
                Species.CAT -> {
                    breeds.clear()
                    catBreeds.forEach { breeds.add(it) }
                    selectedBreed = catBreeds[0]
                }

                Species.BIRD -> {
                    breeds.clear()
                    birdBreeds.forEach { breeds.add(it) }
                    selectedBreed = birdBreeds[0]
                }

                Species.OTHER -> {
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
                                Log.d("pet", selectedBreed)
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
                                    contentDescription = "Akceptuj"
                                )
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDialog.value = false }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Anuluj"
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
                            nameInput, selectedSpecies.name, selectedBreed, millisToLocalDate!!, userId!!

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
                                        "Pomyślnie dodano zwierzę",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(MainLeafScreen.Pet.route)
                                } else {
                                    Toast.makeText(context, "Błąd, spróbuj ponownie", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun onFailure(
                                p0: Call<String>,
                                p1: Throwable
                            ) {
                                Log.d("retrofit", p1.message.toString())
                                Toast.makeText(context, "Błąd połączenia", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        })
                    }

                }, modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 15.dp)
            ) {
                Box(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Dodaj zwierzaka")
                }
            }
    }

}