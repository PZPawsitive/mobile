package com.example.pawsitive.view.main.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.pawsitive.models.Pet
import com.example.pawsitive.models.User
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.viewmodel.ApiViewModel
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun PetInfoScreen(apiViewModel: ApiViewModel) {
    var pet: Pet? by remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)
//    runBlocking {
//        val call: Call<Pet> = apiViewModel.petService.getPetById(preferencesManager.getUserId()!!)
//        call.enqueue(object : Callback<User> {
//            override fun onResponse(
//                p0: Call<User>,
//                p1: Response<User>
//            ) {
//                Log.d("retrofit", p1.body().toString())
//                if (p1.body() != null) {
//                    user = p1.body()
//                } else {
//                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(
//                p0: Call<User>,
//                p1: Throwable
//            ) {
//                Log.d("retrofit", p1.message.toString())
//                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }

    Text(text = "pet info screen")
}