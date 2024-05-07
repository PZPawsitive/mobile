package com.example.pawsitive.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.pawsitive.beacon.ScanDeviceListActivity
import com.example.pawsitive.view.RegisterActivity

// CHECKOUT BRANCH
@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    Column {
        Button(onClick = {
            val intent = Intent(context, ScanDeviceListActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "klikaj")
        }
    }
}