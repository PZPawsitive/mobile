package com.example.pawsitive.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pawsitive.viewmodel.BeaconViewModel
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FollowScreen(
    beaconViewModel: BeaconViewModel
) {

    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
//            PeripheralList(beaconViewModel)

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Follow Screen", style = MaterialTheme.typography.headlineMedium
            )
        }

    }
}

//@Composable
//fun PeripheralList(
//    list: BeaconViewModel
//) {
//    val mlist = list.mlist
////    Log.d("beaconData","mlist: ${mlist.toList()}")
//    LazyColumn(
//        modifier = Modifier.height(50.dp)
//    ) {
//        items(items = mlist) {
//            Card {
//                Text(text = it.toString())
//            }
//        }
//    }
//}
//@Composable
//fun PrintValueEveryFiveSeconds(value: String) {
//    LaunchedEffect(key1 = Unit) {
//        while (true) {
//            Log.d("beaconData", "mlist: $value")
//            delay(1000L)
//        }
//    }
//}