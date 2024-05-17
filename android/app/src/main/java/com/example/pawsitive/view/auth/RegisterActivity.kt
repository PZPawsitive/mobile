package com.example.pawsitive.view.auth

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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


class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            PawsitiveTheme {
//                RegisterView()
//            }
            RegisterView()
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RegisterView() {

        val context = LocalContext.current

        var loginInput by rememberSaveable {
            mutableStateOf("")
        }
        var passwordInput by rememberSaveable {
            mutableStateOf("")
        }
        var repeatPasswordInput by rememberSaveable {
            mutableStateOf("")
        }
        var showRepeatedPassword by rememberSaveable {
            mutableStateOf(false)
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
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                errorBorderColor = Color.Red,
                                errorTrailingIconColor = Color.Black
                            ),
                            isError = passwordInput != repeatPasswordInput,
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword) Icons.Outlined.Lock else Icons.Filled.Lock,
                                        contentDescription = "show password"
                                    )
                                }
                            }
                        )
                        OutlinedTextField(
                            value = repeatPasswordInput,
                            label = { Text(text = "Repeat password") },
                            visualTransformation = if (showRepeatedPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            onValueChange = { repeatPasswordInput = it },
                            modifier = Modifier
                                .fillMaxWidth(),
                            isError = passwordInput != repeatPasswordInput,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                errorBorderColor = Color.Red,
                                errorTrailingIconColor = Color.Black
                            ),
                            supportingText = {
                                if (passwordInput != repeatPasswordInput) {
                                    Text(text = "Password don't match!", textAlign = TextAlign.Center)
                                }
                            },
                            trailingIcon = {
                                IconButton(onClick = { showRepeatedPassword = !showRepeatedPassword }) {
                                    Icon(
                                        imageVector = if (showRepeatedPassword) Icons.Outlined.Lock else Icons.Filled.Lock,
                                        contentDescription = "show password"
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .align(
                                    Alignment.CenterHorizontally
                                )
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                },
                            ) {
                                Text(text = "Already have an account?")
                            }
                            Button(onClick = {}
                            ) {
                                Text(text = "Register")
                            }

                        }

                    }
                }
            }

        }
    }
}