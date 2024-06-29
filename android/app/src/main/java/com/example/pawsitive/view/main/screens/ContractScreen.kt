package com.example.pawsitive.view.main.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pawsitive.models.Contract
import com.example.pawsitive.navigation.main.MainLeafScreen
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.view.walk.WalkActivity
import com.example.pawsitive.viewmodel.ApiViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Map
import kotlinx.coroutines.runBlocking

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter


@Composable
fun ContractScreen(apiViewModel: ApiViewModel, id: String?, navController: NavController) {
    var contract: Contract? by remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    runBlocking {
        val call: Call<Contract> = apiViewModel.walkService.getContract(id = id!!)
        call.enqueue(object : Callback<Contract> {
            override fun onResponse(
                p0: Call<Contract>,
                p1: Response<Contract>
            ) {
                Log.d("retrofit", p1.body().toString())
                if (p1.body() != null) {
                    contract = p1.body()
                } else {
                    Toast.makeText(context, "Błąd, spróbuj ponownie", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                p0: Call<Contract>,
                p1: Throwable
            ) {
                Log.d("retrofit", p1.message.toString())
                Toast.makeText(context, "Błąd połaczenia", Toast.LENGTH_SHORT).show()
            }
        })
    }
    val openAlertDialog = remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    if (contract != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Column {
                val location =
                    Uri.parse("geo:${contract!!.location.latitude},${contract!!.location.longitude}?q=${contract!!.location.latitude},${contract!!.location.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, location)
                val chooser = Intent.createChooser(mapIntent, "Wybierz aplikację z mapą")

                Text(
                    text = "Zlecenie",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(20.dp))
                if (contract!!.owner != null) { // for now
                    Row {
                        Text(
                            text = "Dodane przez: ",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${contract!!.owner!!.firstName} ${contract!!.owner!!.lastName}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(
                        text = "Opis: ",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = contract!!.description,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(
                        text = "Ilość zwierząt: ",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = contract!!.pets.size.toString(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(
                        text = "Wynagrodzenie: ",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${contract!!.reward}zł",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (contract!!.completed) {
                    val date = contract!!.completedAt!!.format(formatter)
                    Row {
                        Text(
                            text = "Ukończone: ",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall,

                            )
                        Text(
                            text = date,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                } else if (contract!!.active) {
                    val date = contract!!.acceptedAt!!.format(formatter)
                    Row {
                        Text(
                            text = "Aktywne od: ",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        Text(
                            text = date,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                } else {
                    val date = contract!!.createdAt.format(formatter)
                    Row {
                        Text(
                            text = "Stworzone: ",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            text = date,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedButton(
                    onClick = {
                        try {
                            context.startActivity(chooser)
                        } catch (e: ActivityNotFoundException) {
                            Toast
                                .makeText(
                                    context,
                                    "Nie można znaleźć aplikacji do obsługi map",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Pokaż lokalizację", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = FontAwesomeIcons.Solid.Map,
                            contentDescription = "Otwórz mapę z lokalizacją",
                            Modifier
                                .size(30.dp, 30.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = {
                        navController.navigate(MainLeafScreen.Chat.route)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Zacznij czat", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "Zacznij czat z właścicielem zlecenia",
                            Modifier
                                .size(30.dp, 30.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }

                }

                when {
                    openAlertDialog.value -> {
                        AlertDialog(
                            onDismissRequest = { openAlertDialog.value = false },
                            confirmButton = {
                                Button(onClick = {
                                    runBlocking {
                                        val call: Call<String> =
                                            apiViewModel.walkService.acceptContract(
                                                id!!,
                                                preferencesManager.getUserId()!!
                                            )
                                        call.enqueue(object : Callback<String> {
                                            override fun onResponse(
                                                p0: Call<String>,
                                                p1: Response<String>
                                            ) {
                                                if (p1.body() != null) {
                                                    openAlertDialog.value = false
                                                    val bundle = Bundle().apply {
                                                        putString("historyId", contract!!.id)
                                                    }
                                                    val intent = Intent(
                                                        context,
                                                        WalkActivity::class.java
                                                    ).apply { putExtras(bundle) }
                                                    context.startActivity(intent)
                                                } else {
                                                    Log.d("retrofit", p1.body().toString())
                                                    Toast.makeText(
                                                        context,
                                                        "Błąd, spróbuj ponownie",
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
                                                    "Błąd połączenia",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        })
                                    }
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
                                Text(text = "Chcesz zaakceptować zlecenie i zacząć spacer?")
                            }
                        )
                    }
                }

            }
            FloatingActionButton(
                onClick = { openAlertDialog.value = !openAlertDialog.value }, modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 15.dp)
            ) {
                Box(modifier = Modifier.padding(15.dp)) {
                    Text(text = "Zaakceptuj zlecenie")
                }
            }

        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }

}