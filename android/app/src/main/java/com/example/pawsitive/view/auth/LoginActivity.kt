package com.example.pawsitive.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawsitive.api.NetworkRepository
import com.example.pawsitive.api.ServiceConfiguration
import com.example.pawsitive.models.LoginRequest
import com.example.pawsitive.models.User
import com.example.pawsitive.view.main.MainActivity
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val networkRepository = NetworkRepository(ServiceConfiguration.userService, ServiceConfiguration.petService, ServiceConfiguration.walkService)

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            LoginView()


        }
    }

    @Composable
    fun LoginView() {

        val context = LocalContext.current

        var loginInput by rememberSaveable {
            mutableStateOf("")
        }
        var passwordInput by rememberSaveable {
            mutableStateOf("")
        }

        var showPassword by rememberSaveable {
            mutableStateOf(false)
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Column {
                Text(
                    text = "Pawsitive",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 50.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(60.dp))
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {

                    Column(

                    ) {
                        OutlinedTextField(
                            value = loginInput,
                            label = { Text(text = "Login") },
                            onValueChange = { loginInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            value = passwordInput,
                            label = { Text(text = "Password") },
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            onValueChange = { passwordInput = it },
                            modifier = Modifier
                                .fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword) Icons.Outlined.Lock else Icons.Filled.Lock,
                                        contentDescription = "show password"
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(context, RegisterActivity::class.java)
                                    startActivity(intent)
                                },

                                ) {
                                Text(text = "Don't have account?")
                            }
                            Button(
                                onClick = {
//                                    val intent = Intent(context, MainActivity::class.java)
//                                    startActivity(intent)
                                    runBlocking {
                                        val call: Call<User> = networkRepository.login(LoginRequest("example@example.com", "admin"))
                                        call.enqueue(object : Callback<User> {
                                            override fun onResponse(
                                                p0: Call<User>,
                                                p1: Response<User>
                                            ) {
                                                Log.d("retrofit", p1.body().toString())
                                                val intent = Intent(context, MainActivity::class.java)
                                                startActivity(intent)
                                            }

                                            override fun onFailure(
                                                p0: Call<User>,
                                                p1: Throwable
                                            ) {
                                                Log.d("retrofit", p1.message.toString())
                                            }

                                        })
                                    }

                                },
                            ) {
                                Text(text = "Login")
                            }

                        }

                    }
                }
            }

        }
    }

}


