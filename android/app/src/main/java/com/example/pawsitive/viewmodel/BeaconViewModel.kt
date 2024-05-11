package com.example.pawsitive.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.minew.beaconplus.sdk.MTPeripheral

@SuppressLint("MutableCollectionMutableState")
class BeaconViewModel : ViewModel(){

//    var mlist by mutableStateOf(mutableStateListOf<MTPeripheral>())

    var _mlist = mutableStateListOf<MTPeripheral>()
    val mlist : List<MTPeripheral> = _mlist

    var connectedMTPeripheral by mutableStateOf<MTPeripheral?>(null)
    fun setBeaconList(list: List<MTPeripheral>) {
        Log.d("beaconData", list.toString())
        _mlist.clear()
        list.forEach {
            _mlist.add(it)
        }
    }

    fun setConnectedPeripheral(mtPeripheral: MTPeripheral) {
        connectedMTPeripheral = mtPeripheral
    }
}