package com.example.pawsitive.view.main.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pawsitive.models.Contract
import com.example.pawsitive.navigation.main.MainLeafScreen
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter

@SuppressLint("UnrememberedMutableState")
@Composable
fun PetHistoryScreen(navController: NavController, apiViewModel: ApiViewModel, petId: String?) {

    Log.d("retrofit", petId.toString())

    val context = LocalContext.current

    val openAlertDialog = remember { mutableStateOf(false) }

    val _histories = remember {
        mutableStateListOf<Contract>()
    }
    val histories: List<Contract?> = _histories
    var selectedHistoryId: String? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(Unit) {
        val call: Call<List<Contract>> =
            apiViewModel.petService.getAllPetWalkHistoryByPetId(id = petId!!)
        call.enqueue(object : Callback<List<Contract>> {
            override fun onResponse(
                p0: Call<List<Contract>>,
                p1: Response<List<Contract>>
            ) {
                Log.d("retrofit", p1.body().toString())
                if (p1.body() != null) {
                    _histories.clear()
                    p1.body()!!.forEach {
                        _histories.add(it)
                    }
                    Log.d("retrofit", "histories ${p1.body()}")
                } else {
                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                p0: Call<List<Contract>>,
                p1: Throwable
            ) {
                Log.d("retrofit", p1.message.toString())
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun loadHistories() {
        runBlocking {
            val call: Call<List<Contract>> =
                apiViewModel.petService.getAllPetWalkHistoryByPetId(id = petId!!)
            call.enqueue(object : Callback<List<Contract>> {
                override fun onResponse(
                    p0: Call<List<Contract>>,
                    p1: Response<List<Contract>>
                ) {
                    Log.d("retrofit", p1.body().toString())
                    if (p1.body() != null) {
                        _histories.clear()
                        p1.body()!!.forEach {
                            _histories.add(it)
                        }
                        Log.d("retrofit", "histories ${p1.body()}")
                    } else {
                        Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    p0: Call<List<Contract>>,
                    p1: Throwable
                ) {
                    Log.d("retrofit", p1.message.toString())
                    Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        if (histories.isNotEmpty()) {
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
                                    text = { Text(text = "Change description") },
                                    onClick = {
                                        selectedHistoryId = it!!.id
                                        expandedSettings = !expandedSettings
                                        openAlertDialog.value = !openAlertDialog.value
                                        Log.d("retrofit", selectedHistoryId.toString())
                                    })
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text(text = "remove history") },
                                    onClick = {
                                        expandedSettings = !expandedSettings
                                        runBlocking {
                                            val call: Call<String> =
                                                apiViewModel.walkService.deleteContract(it!!.id)
                                            call.enqueue(object : Callback<String> {
                                                override fun onResponse(
                                                    p0: Call<String>,
                                                    p1: Response<String>
                                                ) {
                                                    Log.d("retrofit", p1.body().toString())
                                                    if (p1.body() != null) {
                                                        _histories.remove(it)
                                                        Log.d("retrofit", "histories ${p1.body()}")
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Error, try again",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }

                                                override fun onFailure(
                                                    p0: Call<String>,
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
                                    })
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text(text = "Show on map") },
                                    onClick = { navController.navigate("${MainLeafScreen.PetHistoryMap.route}?id=${it!!.id}") })
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                onClick = { expandedSettings = !expandedSettings }
                            ) {
                                Box(modifier = Modifier.padding(10.dp)) {
                                    Column(modifier = Modifier.padding(5.dp)) {
                                        Text(text = it!!.description)
                                        val formatter =
                                            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                                        if (it.completed) {
                                            val date = it.completedAt!!.format(formatter)
                                            Row {
                                                Text(
                                                    text = "Completed at: ",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                                Text(
                                                    text = date,
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                            }

                                        } else if (it.active) {
                                            val date = it.acceptedAt!!.format(formatter)
                                            Row {
                                                Text(
                                                    text = "Active since: ",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.labelLarge
                                                )

                                                Text(
                                                    text = date,
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                            }

                                        } else {
                                            val date = it.createdAt.format(formatter)
                                            Row {
                                                Text(
                                                    text = "created at: ",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                                Text(
                                                    text = date,
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                            }

                                        }

                                    }
                                }

                            }
                        }

                    }
                }
            }
        } else {
            Text(
                text = "Brak spacerów",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
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
                            Log.d("retrofit", "selected id: ${selectedHistoryId!!}")
                            runBlocking {
                                val call: Call<String> = apiViewModel.walkService.changeDescription(
                                    selectedHistoryId!!,
                                    input
                                )
                                call.enqueue(object : Callback<String> {
                                    override fun onResponse(
                                        p0: Call<String>,
                                        p1: Response<String>
                                    ) {
                                        Log.d("retrofit", p1.body().toString())
                                        if (p1.body() != null) {
                                            loadHistories()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Error, try again",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(
                                        p0: Call<String>,
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
                        OutlinedTextField(value = input, onValueChange = {
                            input = it
                            Log.d("retrofit", selectedHistoryId!!)
                        })
                    }
                )
            }
        }
    }


}