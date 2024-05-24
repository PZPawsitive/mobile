package com.example.pawsitive.view.main.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pawsitive.models.Contract
import com.example.pawsitive.navigation.main.MainLeafScreen
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContractListScreen(navController: NavHostController, apiViewModel: ApiViewModel) {

    Box(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        var contracts: List<Contract>? by remember {
            mutableStateOf(null)
        }
        runBlocking {
            val call: Call<List<Contract>> = apiViewModel.walkService.getContracts()
            call.enqueue(object : Callback<List<Contract>> {
                override fun onResponse(
                    p0: Call<List<Contract>>,
                    p1: Response<List<Contract>>
                ) {
                    Log.d("retrofit", p1.body().toString())
                    if (p1.body() != null) {
                        contracts = p1.body()!!.filter { !it.active && !it.completed }
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
        if (contracts != null) {
            LazyColumn(
                modifier = Modifier.padding(10.dp)
            ) {
                items(items = contracts!!) {
                    var expandedSettings by remember {
                        mutableStateOf(false)
                    }
                    Box() {
                        Card(
                            onClick = { expandedSettings = !expandedSettings },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            colors = if (it.dangerous) CardDefaults.cardColors(containerColor = Color.Red) else CardDefaults.cardColors(
                                containerColor = Color.Green
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(text = it.description, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                        Text(text = if (it.pets.size == 1) "${it.pets.size} pet" else "${it.pets.size} pets", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                                        Text(text = "${it.reward} $", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                                    }
                                }


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
                                onClick = { navController.navigate("${MainLeafScreen.ContractScreen.route}?id=${it.id}") })
                        }
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
        FloatingActionButton(
            onClick = { navController.navigate(MainLeafScreen.ContractAddForm.route) },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add contract")
        }
    }
}


