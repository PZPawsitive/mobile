package com.example.pawsitive.view.main.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pawsitive.models.Contract
import com.example.pawsitive.models.Pet
import com.example.pawsitive.view.walk.WalkActivity
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking

import org.osmdroid.util.GeoPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



@Composable
fun ContractScreen(apiViewModel: ApiViewModel, id: String?) {
    WalkNotActiveView(apiViewModel,id)
}

@Composable
fun WalkNotActiveView(apiViewModel: ApiViewModel,id: String?) {
    var contract: Contract? by remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current

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
                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                p0: Call<Contract>,
                p1: Throwable
            ) {
                Log.d("retrofit", p1.message.toString())
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
            }

        })
    }
    val openAlertDialog = remember { mutableStateOf(false) }
    if (contract != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
//            val location =
//                Uri.parse("geo:${contract.localization.latitude},${exampleContract.localization.longitude}?q=${exampleContract.localization.latitude},${exampleContract.localization.longitude}")
//            val mapIntent = Intent(Intent.ACTION_VIEW, location)
//            val chooser = Intent.createChooser(mapIntent, "choose map")

                Text(text = "Contract", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(10.dp))
//                Text(text = "Owner: ${contract!!.user}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,  style = MaterialTheme.typography.headlineSmall)
//                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Pet quantity: ${contract!!.pets.size}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,  style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Payment: ${contract!!.reward}$", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,  style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
//                try {
//                    context.startActivity(chooser)
//                } catch (e: ActivityNotFoundException) {
//                    Toast.makeText(
//                        context,
//                        "Cannot find application to handle maps",
//                        Toast.LENGTH_LONG
//                    )
//                        .show()
//                }
                        }, horizontalArrangement = Arrangement.Center) {
                    Text(text = "Show location", style = MaterialTheme.typography.headlineSmall) //
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "open map with localization",
                        Modifier
                            .size(30.dp, 30.dp)
                            .align(Alignment.CenterVertically))
                }
                Spacer(modifier = Modifier.height(10.dp))

                when {
                    openAlertDialog.value -> {
                        AlertDialog(
                            onDismissRequest = { openAlertDialog.value = false },
                            confirmButton = {
                                Button(onClick = {
                                    runBlocking {
                                        val call: Call<String> = apiViewModel.walkService.acceptContract(id!!)
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
                                                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            override fun onFailure(
                                                p0: Call<String>,
                                                p1: Throwable
                                            ) {
                                                Log.d("retrofit", p1.message.toString())
                                                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
                                            }

                                        })
                                    }
                                }) {
                                    Text(text = "Yes")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { openAlertDialog.value = false }) {
                                    Text(text = "No")
                                }
                            },
                            text = {
                                Text(text = "Do you want to accept the contract and start the walk?")
                            }
                        )
                    }
                }

            }
            FloatingActionButton(onClick = { openAlertDialog.value = !openAlertDialog.value }, modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 15.dp)) {
                Box(modifier = Modifier.padding(15.dp)) {
                    Text(text = "Accept contract")
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