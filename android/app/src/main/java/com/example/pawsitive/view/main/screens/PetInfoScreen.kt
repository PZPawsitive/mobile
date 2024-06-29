package com.example.pawsitive.view.main.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pawsitive.models.Pet
import com.example.pawsitive.navigation.main.MainLeafScreen
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.viewmodel.ApiViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cat
import compose.icons.fontawesomeicons.solid.Dog
import compose.icons.fontawesomeicons.solid.Dove
import compose.icons.fontawesomeicons.solid.Horse
import compose.icons.fontawesomeicons.solid.Paw
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun PetInfoScreen(apiViewModel: ApiViewModel, petId: String?, navController: NavController) {
    var pet: Pet? by remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
    runBlocking {
        val call: Call<Pet> = apiViewModel.petService.getPetById(id = petId!!)
        call.enqueue(object : Callback<Pet> {
            override fun onResponse(
                p0: Call<Pet>,
                p1: Response<Pet>
            ) {
                Log.d("retrofit", p1.body().toString())
                if (p1.body() != null) {
                    pet = p1.body()
                } else {
                    Toast.makeText(context, "Błąd, spróbuj ponownie", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                p0: Call<Pet>,
                p1: Throwable
            ) {
                Log.d("retrofit", p1.message.toString())
                Toast.makeText(context, "Błąd połączenia", Toast.LENGTH_SHORT).show()
            }

        })
    }
    if (pet != null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = when (pet!!.species) {
                "DOG" -> FontAwesomeIcons.Solid.Dog
                "CAT" -> FontAwesomeIcons.Solid.Cat
                "BIRD" -> FontAwesomeIcons.Solid.Dove
                "HORSE" -> FontAwesomeIcons.Solid.Horse
                else -> FontAwesomeIcons.Solid.Paw
            }

            , contentDescription = "Ikona zwierzęcia", Modifier.size(200.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = pet!!.name, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(text = "Rasa: ", fontWeight = FontWeight.Bold)
                Text(text = pet!!.breed)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(text = "Urodzone: ", fontWeight = FontWeight.Bold)
                Text(text = pet!!.birthdate.toString())
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { navController.navigate("${MainLeafScreen.PetHistory.route}?petId=${pet!!.id}") }) {
                    Text(text = "Pokaż historię spacerów")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Edytuj")
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