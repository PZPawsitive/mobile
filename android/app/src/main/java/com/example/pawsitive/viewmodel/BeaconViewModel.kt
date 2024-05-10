package com.example.pawsitive.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.minew.beaconplus.sdk.MTPeripheral

@SuppressLint("MutableCollectionMutableState")
class BeaconViewModel : ViewModel(){

//    var mlist by mutableStateOf(mutableStateListOf<MTPeripheral>())

    var _mlist = mutableStateListOf<MTPeripheral>()
    val mlist : List<MTPeripheral> = _mlist

    fun setBeaconList(list: List<MTPeripheral>) {
        _mlist.clear()
        list.forEach {
            _mlist.add(it)
        }
    }
}