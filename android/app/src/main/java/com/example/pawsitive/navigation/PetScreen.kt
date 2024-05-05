package com.example.pawsitive.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cat
import compose.icons.fontawesomeicons.solid.Dog

enum class PetType {
    DOG,
    CAT
}

data class Pet(val name: String, val type: PetType)

val pets =
    listOf(
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
        Pet("Kacper", PetType.DOG),
        Pet("Franciszek", PetType.CAT),
        Pet("Bogusław", PetType.DOG),
    )

// CHECKOUT BRANCH
@Composable
fun PetScreen() {
    LazyColumn() {
        items(items = pets) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = it.name)
                    Icon(
                        imageVector = if (it.type == PetType.DOG) FontAwesomeIcons.Solid.Dog else FontAwesomeIcons.Solid.Cat,
                        contentDescription = "pet type",
                        Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}