package com.example.pawsitive.walkactivityviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Message(val owner: String, val content: String)

@Composable
fun MessageScreen() {
    var inputText by remember { mutableStateOf("") }
    val rememberedMessages = remember {
        mutableStateListOf(
            Message("owner", "chciałbym, żebyś wyprowadził mi pieska"),
            Message("me", "okej, wyprowadzę"),
            Message("owner", "wynagrodzenie prześlę blikiem"),
            Message("owner", "podaj mi swojego blika"),
            Message("me", "111 111 111"),
            Message("owner", "zapłata po spacerze"),
            Message("me", "super"),
            Message("me", "to wychodzę z pieskiem")
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                items(rememberedMessages) { message ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if (message.owner == "owner") Arrangement.Start else Arrangement.End) {
                        Card(onClick = { /*TODO*/ }, modifier = Modifier.widthIn(max = 250.dp)) {
                            Text(text = message.content, modifier = Modifier.padding(10.dp))
                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                )
                Icon(
                    imageVector = Icons.Filled.Send,  // Asumując, że masz odpowiednią ikonę
                    contentDescription = "send message",
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            if (inputText.isNotEmpty()) {
                                rememberedMessages.add(Message("me", inputText))
                                inputText = ""  // Resetowanie pola tekstowego po wysłaniu
                            }
                        }
                )
            }
        }
    }
}