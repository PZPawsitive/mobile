package com.example.pawsitive.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.minew.beaconplus.sdk.MTPeripheral

@SuppressLint("MutableCollectionMutableState")
class BeaconViewModel : ViewModel(){



//    var mlist by mutableStateOf(mutableStateListOf<MTPeripheral>())

    var _mlist = mutableStateListOf<MTPeripheral>()
    val mlist : List<MTPeripheral> = _mlist

    var connectedMTPeripheral by mutableStateOf<MTPeripheral?>(null)

    var _listenedDevices = mutableStateListOf<MTPeripheral>()

    val listenedDevices: List<MTPeripheral> = _listenedDevices

    fun setBeaconList(list: List<MTPeripheral>) {
//        Log.d("beaconData", list.toString())
        _mlist.clear()
        list.forEach {
            _mlist.add(it)
        }
    }

    fun addListenedDevice(device: MTPeripheral) {
        _listenedDevices.add(device)
    }
    fun removeListenedDevice(device: MTPeripheral) {
        _listenedDevices.remove(device)
    }

    fun setListenedList(list: List<MTPeripheral>) {
        _listenedDevices.clear()
        list.forEach {
            _listenedDevices.add(it)
        }
    }

    fun setConnectedPeripheral(mtPeripheral: MTPeripheral) {
        connectedMTPeripheral = mtPeripheral
    }
}