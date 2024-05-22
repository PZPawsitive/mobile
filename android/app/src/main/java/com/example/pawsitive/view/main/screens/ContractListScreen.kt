package com.example.pawsitive.view.main.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.pawsitive.viewmodel.ApiViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContractListScreen(navController: NavHostController, apiViewModel: ApiViewModel) {

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