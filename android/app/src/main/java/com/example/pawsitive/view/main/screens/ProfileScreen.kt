package com.example.pawsitive.view.main.screens


import android.content.Intent
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
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
import coil.compose.AsyncImage
import com.example.pawsitive.models.LoginRequest
import com.example.pawsitive.models.User
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.view.main.MainActivity
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun ProfileScreen(apiViewModel: ApiViewModel) {

    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    Log.d("retrofit", preferencesManager.getToken().toString())
    Log.d("retrofit", preferencesManager.getUserId().toString())
    var user: User? by remember {
        mutableStateOf(null)
    }

    runBlocking {
        val call: Call<User> = apiViewModel.userService.getUserById(preferencesManager.getUserId()!!)
        call.enqueue(object : Callback<User> {
            override fun onResponse(
                p0: Call<User>,
                p1: Response<User>
            ) {
                Log.d("retrofit", p1.body().toString())
                if (p1.body() != null) {
                    user = p1.body()
                } else {
                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                p0: Call<User>,
                p1: Throwable
            ) {
                Log.d("retrofit", p1.message.toString())
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    if (user != null) {
        UserProfile(user = user!!)
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp).align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

    }



}

@Composable
fun UserProfile(user: User) {
    Column {
        AsyncImage(
            model = user.profilePic,
            contentDescription = "user profile picture",
            modifier = Modifier.padding(15.dp)
        )
        Text(text = "${user.firstName} ${user.lastName}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineLarge)
        Text(text = "Description: ${user.description}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineSmall)
        Text(text = "Email: ${user.email}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineSmall)
        Text(text = "Phone number: ${user.phone}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineSmall)
        Text(text = "Birthdate: ${user.birthdate}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineSmall)
        Text(text = user.role!!, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineSmall)
    }
}