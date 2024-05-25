package com.example.pawsitive.view.main.screens


import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pawsitive.models.User
import com.example.pawsitive.models.UserDTO
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun ProfileScreen(apiViewModel: ApiViewModel) {

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    var user: UserDTO? by remember {
        mutableStateOf(null)
    }

    fun loadData() {
        runBlocking {
            val call: Call<UserDTO> =
                apiViewModel.userService.getUserById(preferencesManager.getUserId()!!)
            call.enqueue(object : Callback<UserDTO> {
                override fun onResponse(
                    p0: Call<UserDTO>,
                    p1: Response<UserDTO>
                ) {
                    Log.d("retrofit", "data")
                    Log.d("retrofit", p1.body().toString())
                    if (p1.body() != null) {
                        user = p1.body()
                    } else {
                        Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    p0: Call<UserDTO>,
                    p1: Throwable
                ) {
                    Log.d("retrofit", p1.message.toString())
                    Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }


    LaunchedEffect(Unit) {
        loadData()
        delay(15000)
        loadData()
    }
    Log.d("retrofit", "recompose")


    if (user != null) {
        UserProfile(user = user!!, apiViewModel, ::loadData)
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

@Composable
fun UserProfile(user: UserDTO, apiViewModel: ApiViewModel, loadData: () -> Unit) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    Box(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize()
    ) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row {
                    AsyncImage(
                        model = user.profilePic,
                        contentDescription = "user profile picture",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(300.dp)
                            .clip(shape = RoundedCornerShape(150.dp))
                    )
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "${user.firstName} ${user.lastName}",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Description: ${user.description}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Email: ${user.email}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Phone number: ${user.phone}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Birthdate: ${user.birthdate}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = user.profilePic,
                        contentDescription = "user profile picture",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(300.dp)
                            .clip(shape = RoundedCornerShape(150.dp))
                    )
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "${user.firstName} ${user.lastName}",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Description: ${user.description}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Email: ${user.email}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Phone number: ${user.phone}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Birthdate: ${user.birthdate}",
                            style = MaterialTheme.typography.headlineSmall
                        )
//                        if (user.dogWalker != null && user.dogWalker) {
//                            Text(
//                                text = "Dog walker",
//                                style = MaterialTheme.typography.headlineSmall
//                            )
//                        } else {
//                            Text(
//                                text = "Not dog walker",
//                                style = MaterialTheme.typography.headlineSmall
//                            )
//                        }
                    }

                }
            }
        }
        FloatingActionButton(
            onClick = {
                runBlocking {
                    val call: Call<String> =
                        apiViewModel.userService.updateDogwalker(preferencesManager.getUserId()!!)
                    call.enqueue(object : Callback<String> {
                        override fun onResponse(
                            p0: Call<String>,
                            p1: Response<String>
                        ) {
                            Log.d("retrofit", p1.body().toString())
                            if (p1.body() != null) {
                                loadData()
                                Toast.makeText(
                                    context,
                                    "Updated!",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                            Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }, modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 15.dp)
        ) {
            Box(modifier = Modifier.padding(10.dp)) {
                Text(text = if (user.dogWalker != null && user.dogWalker) "Quit dog walking" else "Become dog walker")
            }

        }
        FloatingActionButton(
            onClick = { /*TODO*/ }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 15.dp)
        ) {
            Box(modifier = Modifier.padding(10.dp)) {
                Text(text = "Edit Info")
            }

        }
    }


}