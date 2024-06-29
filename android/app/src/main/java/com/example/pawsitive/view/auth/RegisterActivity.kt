package com.example.pawsitive.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.pawsitive.models.RegisterRequest
import com.example.pawsitive.models.SimpleGeopoint
import com.example.pawsitive.models.User
import com.example.pawsitive.util.DateUtils
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime


class RegisterActivity : ComponentActivity() {
    private lateinit var apiViewModel: ApiViewModel
    private lateinit var preferencesManager: PreferencesManager

    var registerView = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ApiViewModel::class.java]
        preferencesManager = PreferencesManager(applicationContext)
        preferencesManager.clear()
        setContent {
//            PawsitiveTheme {
//                RegisterView()
//            }
//
            RegisterView()
        }
    }

    @Composable
    fun RegisterView() {
        if (registerView.value) {
            RegisterScreen()
        } else {
            VerificationScreen()
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RegisterScreen() {
        val context = LocalContext.current

        var emailInput by rememberSaveable {
            mutableStateOf("")
        }
        var firstNameInput by rememberSaveable {
            mutableStateOf("")
        }
        var lastNameInput by rememberSaveable {
            mutableStateOf("")
        }
        var phoneInput by rememberSaveable {
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
        var showDialog = rememberSaveable {
            mutableStateOf(false)
        }
        val birthDate = rememberDatePickerState()
        val millisToLocalDate = birthDate.selectedDateMillis?.let {
            DateUtils().convertMillisToLocalDate(it)
        }
        val dateToString = birthDate.selectedDateMillis?.let {
            DateUtils().dateToString(millisToLocalDate!!)
        } ?: "Wybierz datę urodzenia"
        var isPhoneValid by rememberSaveable {
            mutableStateOf(true)
        }
        val phoneRegex = "^\\d{9}$".toRegex()

        var phoneInputTouched by rememberSaveable {
            mutableStateOf(false)
        }
        var phoneInputFocused by rememberSaveable {
            mutableStateOf(false)
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        var emailInputTouched by rememberSaveable {
            mutableStateOf(false)
        }
        var emailInputFocused by rememberSaveable {
            mutableStateOf(false)
        }
        var isEmailValid by rememberSaveable {
            mutableStateOf(true)
        }

        fun canRegister(): Boolean = (
                passwordInput == repeatPasswordInput
                        && isPhoneValid
                        && isEmailValid
                        && phoneInputTouched
                        && emailInputTouched
                        && firstNameInput.isNotEmpty()
                        && lastNameInput.isNotEmpty()
                        && birthDate.selectedDateMillis != null
                )

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
                            value = firstNameInput,
                            label = { Text(text = "Imię") },
                            onValueChange = { firstNameInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = lastNameInput,
                            label = { Text(text = "Nazwisko") },
                            onValueChange = { lastNameInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            value = emailInput,
                            label = { Text(text = "E-mail") },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                errorBorderColor = Color.Red,
                                errorTrailingIconColor = Color.Black
                            ),
                            onValueChange = {
                                emailInput = it
                                if (emailInputTouched) {
                                    isEmailValid = emailRegex.matches(it)
                                }
                            },
                            singleLine = true,
                            isError = (emailInputTouched && !isEmailValid),
                            supportingText = {
                                if (!isEmailValid) {
                                    Text(
                                        text = "E-mail musi być w formacie: x@x",
                                        textAlign = TextAlign.Center
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged {
                                    emailInputFocused = it.isFocused
                                    if (emailInputFocused) {
                                        emailInputTouched = true
                                        isEmailValid = emailRegex.matches(emailInput)
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            value = passwordInput,
                            label = { Text(text = "Hasło") },
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
                                        contentDescription = "Pokaż hasło"
                                    )
                                }
                            }
                        )
                        OutlinedTextField(
                            value = repeatPasswordInput,
                            label = { Text(text = "Powtórz hasło") },
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
                                    Text(
                                        text = "Hasła są inne!",
                                        textAlign = TextAlign.Center
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    showRepeatedPassword = !showRepeatedPassword
                                }) {
                                    Icon(
                                        imageVector = if (showRepeatedPassword) Icons.Outlined.Lock else Icons.Filled.Lock,
                                        contentDescription = "Pokaż hasło"
                                    )
                                }
                            }
                        )
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
                                contentDescription = "wybór daty",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        OutlinedTextField(
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            value = phoneInput,
                            label = { Text(text = "Numer telefonu") },
                            onValueChange = {
                                phoneInput = it
                                if (phoneInputTouched) {
                                    isPhoneValid = phoneRegex.matches(it)
                                }
                            },
                            supportingText = {
                                if (!isPhoneValid) {
                                    Text(
                                        text = "Numer musi być w formacie: 123456789",
                                        textAlign = TextAlign.Center
                                    )
                                }
                            },
                            singleLine = true,
                            isError = (phoneInputTouched && !isPhoneValid),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                errorBorderColor = Color.Red,
                                errorTrailingIconColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged {
                                    phoneInputFocused = it.isFocused
                                    if (phoneInputFocused) {
                                        phoneInputTouched = true
                                        isPhoneValid = phoneRegex.matches(phoneInput)
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
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
                                Text(text = "Masz już konto?")
                            }
                            Button(
                                onClick = {
                                    runBlocking {
                                        val call: Call<User> = apiViewModel.userService.register(
                                            RegisterRequest(
                                                firstName = firstNameInput.replaceFirstChar(Char::titlecase),
                                                lastName = lastNameInput.replaceFirstChar(Char::titlecase),
                                                email = emailInput,
                                                password = passwordInput,
                                                birthdate = millisToLocalDate!!,
                                                phone = phoneInput.toInt(),
                                                description = "Nowy użytkownik",
                                                profilePic = "https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                                role = "USER_NOT_VERIFIED",
                                                location = SimpleGeopoint(54.39555994665233, 18.5737934140649, LocalDateTime.now())
                                            )
                                        )
                                        call.enqueue(object : Callback<User> {
                                            override fun onResponse(
                                                p0: Call<User>,
                                                p1: Response<User>
                                            ) {
                                                Log.d("retrofit", p1.body().toString())
                                                Log.d("retrofit", p1.message().toString())
                                                Log.d("retrofit", p1.errorBody().toString())
                                                if (p1.body() != null) {
                                                    preferencesManager.saveToken(p1.body()!!.token!!)
                                                    preferencesManager.setUserId(p1.body()!!.id.toString())
                                                    preferencesManager.setEmail(p1.body()!!.email)
                                                    registerView.value = false
                                                } else {

                                                    Log.d("retrofit", p1.body().toString())
                                                    Toast.makeText(context, "Błąd, spróbuj ponownie", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                            override fun onFailure(
                                                p0: Call<User>,
                                                p1: Throwable
                                            ) {
                                                Log.d("retrofit", p1.message.toString())
                                                Toast.makeText(context, "Błąd połączenia", Toast.LENGTH_SHORT).show()
                                            }

                                        })
                                    }
                                },
                                enabled = canRegister(),
                            ) {
                                Text(text = "Zarejestruj")
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
                                                contentDescription = "Potwierdź"
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
                }
            }

        }
    }

    @Composable
    fun VerificationScreen() {
        val context = LocalContext.current
        var tokenInput by rememberSaveable {
            mutableStateOf("")
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.Center) {
                    Text(text = "Kod weryfikacyjny został wysłany na podany e-mail, wpisz go by zweryfikować konto", textAlign = TextAlign.Center)
                }
                Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.Center) {
                    OutlinedTextField(value = tokenInput, onValueChange = {tokenInput = it})
                }
                Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    OutlinedButton(onClick = {
                        runBlocking {
                            val call: Call<String> = apiViewModel.userService.resend(preferencesManager.getEmail()!!)
                            call.enqueue(object : Callback<String> {
                                override fun onResponse(
                                    p0: Call<String>,
                                    p1: Response<String>
                                ) {
                                    if (p1.code() == 200) {
                                        Toast.makeText(context, p1.body().toString(), Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Nie można wysłać, spróbuj ponownie", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(
                                    p0: Call<String>,
                                    p1: Throwable
                                ) {
                                    Toast.makeText(context, "Błąd połączenia", Toast.LENGTH_SHORT).show()
                                }

                            })
                        }
                    }) {
                        Text(text = "Wyślij ponownie")
                    }
                    Button(enabled = tokenInput.isNotEmpty(),onClick = {
                        Log.d("retrofit", "$tokenInput ${preferencesManager.getEmail()}")
                        runBlocking {
                            val call: Call<String> = apiViewModel.userService.verify(tokenInput, preferencesManager.getEmail()!!)
                            call.enqueue(object : Callback<String> {
                                override fun onResponse(
                                    p0: Call<String>,
                                    p1: Response<String>
                                ) {
                                    if (p1.code() == 200) {
                                        val intent = Intent(context, LoginActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(context, "Nie można zweryfikować, spróbuj ponownie", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(
                                    p0: Call<String>,
                                    p1: Throwable
                                ) {
                                    Toast.makeText(context, "Błąd połączenia", Toast.LENGTH_SHORT).show()
                                }

                            })
                        }
                    }) {
                        Text(text = "Zweryfikuj")
                    }
                }
            }

        }
    }
}