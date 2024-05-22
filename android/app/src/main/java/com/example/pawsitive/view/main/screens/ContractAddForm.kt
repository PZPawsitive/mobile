package com.example.pawsitive.view.main.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pawsitive.models.AddPetRequest
import com.example.pawsitive.models.Pet
import com.example.pawsitive.navigation.main.MainLeafScreen
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ContractAddForm(navController: NavController, apiViewModel: ApiViewModel) {

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    var descriptionInput by rememberSaveable {
        mutableStateOf("")
    }
    var rewardInput by rememberSaveable {
        mutableStateOf("")
    }
    val latitude = 0.0
    val longitude = 0.0

    var isDangerous by rememberSaveable {
        mutableStateOf(false)
    }
    var pets: List<Pet>? by remember {
        mutableStateOf(null)
    }
    runBlocking {
        val call: Call<List<Pet>> =
            apiViewModel.petService.getPetsByUserId(preferencesManager.getUserId()!!)
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

    Box(modifier = Modifier.fillMaxSize()) {
        var selectedPets = remember {
            mutableListOf<String?>()
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Description", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = descriptionInput,
                onValueChange = { descriptionInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Reward", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = rewardInput,
                onValueChange = { rewardInput = it },
                modifier = Modifier.width(100.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Is any of pets aggresive?")
            Switch(checked = isDangerous, onCheckedChange = { isDangerous = !isDangerous })
            Spacer(modifier = Modifier.height(20.dp))
            if (pets != null) {
                LazyColumn {
                    items(items = pets!!) {
                        var selected by rememberSaveable {
                            mutableStateOf(true)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selected, onClick = {
                                selected = !selected
                                if (selected) {
                                    selectedPets.add(it.id)
                                } else {
                                    selectedPets.remove(it.id)
                                }
                                Log.d("retrofit", "pets: ${selectedPets}")
                            })
                            Text(text = it.name, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            } else {
                Row {
                    Text(text = "No pets")
                    OutlinedButton(onClick = { navController.navigate(MainLeafScreen.PetAddForm.route) }) {
                        Text(text = "Add pet")
                    }
                }

            }

        }
        if (descriptionInput.isNotEmpty() && rewardInput.isNotEmpty() && selectedPets.toList().isNotEmpty())
            FloatingActionButton(
                onClick = {
                    Log.d("retrofit", "pets: ${selectedPets}")
                    Log.d("retrofit", "dangerous: ${isDangerous}")
                    runBlocking {
                        val userId = preferencesManager.getUserId()
                        val call: Call<String> = apiViewModel.walkService.addContract(
                            description = descriptionInput,
                            reward = rewardInput.toDouble(),
                            latitude = latitude.toDouble(),
                            longitude = longitude.toDouble(),
                            pets = selectedPets.toList(),
                            isDangerous = isDangerous
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
                                        "Succesfully added contract",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(MainLeafScreen.ContractList.route)
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