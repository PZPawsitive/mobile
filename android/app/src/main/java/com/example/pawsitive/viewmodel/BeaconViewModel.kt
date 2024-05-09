package com.example.pawsitive.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.minew.beaconplus.sdk.MTPeripheral

@SuppressLint("MutableCollectionMutableState")
class BeaconViewModel : ViewModel(){

    var mlist by mutableStateOf(mutableStateListOf<MTPeripheral>())

    fun getBeaconList() {
        mlist = mlist
    }

    fun setBeaconList(list: List<MTPeripheral>) {
        mlist = list.toMutableStateList()
    }
}