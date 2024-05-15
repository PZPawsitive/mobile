package com.example.pawsitive.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


// CHECKOUT BRANCH
data class Message(val sender: String, val content: String)

val messages = listOf(
    Message("Piłsudski", "wygralismy"),
    Message("Jan 3 Sobieski", "Miałem 3 pieski"),
    Message("Pepe", "Jestem smutny"),
    Message("Piłsudski", "wygralismy"),
    Message("Jan 3 Sobieski", "Miałem 3 pieski"),
    Message("Pepe", "Jestem smutny"),
    Message("Piłsudski", "wygralismy"),
    Message("Jan 3 Sobieski", "Miałem 3 pieski"),
    Message("Pepe", "Jestem smutny"),
    Message("Piłsudski", "wygralismy"),
    Message("Jan 3 Sobieski", "Miałem 3 pieski"),
    Message("Pepe", "Jestem smutny"),
)


@Composable
fun MessagesScreen(navController: NavController) {
    LazyColumn {
        items(items = messages) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onClick = { navController.navigate(LeafScreen.Chat.route) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                ) {
                    Box(modifier = Modifier
                        .background(shape = CircleShape, color = Color.Red)
                        .padding(20.dp)) {
//                        Text(text = "cos")
                    }
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(text = it.sender, fontWeight = FontWeight.Bold)
                        Text(text = it.content)
                    }
                }
            }
        }
    }
}