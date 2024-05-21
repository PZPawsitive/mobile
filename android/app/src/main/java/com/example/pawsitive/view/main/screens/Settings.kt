package com.example.pawsitive.view.main.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun Settings(navController: NavController, changeState: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Dog walker mode:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            Switch(checked = LocalGlobalState.current, onCheckedChange = { changeState() })
        }
    }
}