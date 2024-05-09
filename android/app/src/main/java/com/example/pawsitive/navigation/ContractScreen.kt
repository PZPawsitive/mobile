package com.example.pawsitive.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

val exampleContract = Contract("kacper", 15.6, 3, true)

@Composable
fun ContractScreen() {
    Column {
        Text(text = "Zlecenie")
        Text(text = "Właściciel: ${exampleContract.owner}")
        Text(text = "Ilość psów: ${exampleContract.petNumber}")
        Text(text = "Wynagrodzenie: ${exampleContract.price}")
        Text(text = "Udaj się w Lokalizację: TODO") // todo onClick - open map with localication

        Button(onClick = { /*TODO*/ }) {
            Text(text = "Rozpocznij spacer")
        }
    }
}