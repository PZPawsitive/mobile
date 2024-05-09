package com.example.pawsitive.navigation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pawsitive.view.LocalBeaconViewModel
import com.example.pawsitive.view.MainActivity
import com.example.pawsitive.viewmodel.BeaconViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FollowScreen() {
    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            PeripheralList()

//            Text(
//                modifier = Modifier.align(Alignment.Center),
//                text = "Follow Screen", style = MaterialTheme.typography.headlineMedium
//            )
        }
        // CHECKOUT BRANCH
    }
}

@Composable
fun PeripheralList() {

//    val viewModel: BeaconViewModel? = LocalViewModelStoreOwner.current?.let { viewModel(it) }
    val beaconViewModel = LocalBeaconViewModel.current
    LazyColumn(
        modifier = Modifier.height(50.dp)
    ) {

//        if (viewModel != null) {
        items(items = beaconViewModel.mlist) {
            Card {
                Text(text = it.toString())
            }

//            }
        }
    }
}