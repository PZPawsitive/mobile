package com.example.pawsitive.beacon

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.pawsitive.R
import com.minew.beaconplus.sdk.MTCentralManager
import com.minew.beaconplus.sdk.MTPeripheral
import com.minew.beaconplus.sdk.Utils.BLETool
import com.minew.beaconplus.sdk.enums.ConnectionStatus
import com.minew.beaconplus.sdk.exception.MTException
import com.minew.beaconplus.sdk.interfaces.ConnectionStatueListener
import com.minew.beaconplus.sdk.interfaces.GetPasswordListener
import com.minew.beaconplus.sdk.interfaces.MTCentralManagerListener
import com.permissionx.guolindev.PermissionX
//import androidx.compose.runtime.*


class ScanDeviceListActivity : AppCompatActivity() {
    val mObjectAnimator: ObjectAnimator? = null

    lateinit var mMTCentralManager: MTCentralManager

    @Composable
    fun PeripheralList(mlist2: List<MTPeripheral>?) {
        val rememberList = remember {
            mutableStateListOf<MTPeripheral>()
        }

        LazyColumn(
            modifier = Modifier.height(50.dp)
        ) {
            items(items = mlist) {
                Card {
                    Text(text = it.toString())
                }

            }
        }
    }

//            private var mlist: List<MTPeripheral>? = null
//    var mlist by mutableStateOf(emptyList<MTPeripheral>())
//    var mlist by mutableStateListOf<MTPeripheral>(emptyList<MTPeripheral>())
//    var mlist = mutableStateListOf<MTPeripheral>()
    var mlist by mutableStateOf(mutableStateListOf<MTPeripheral>())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBleManager()
        setBleManagerListener()
        initBlePermission()
        Log.d("list", mlist.toString())
        setContent {
            Column {

//                LazyColumn {
//                    items(items = mlist!!) {
//                        Text(text = it.toString())
//
//                    }
//                }
                PeripheralList(mlist)

                Button(onClick = { checkItems() }) {
                    Text(text = "Check Items")
                }
            }

        }
    }

    fun initBleManager() {
        mMTCentralManager = MTCentralManager.getInstance(this)
        mMTCentralManager.startService()
    }

    fun setBleManagerListener() {
        mMTCentralManager.setMTCentralManagerListener {
            Log.d("items", it.toString())
            mlist = it.toMutableStateList()
        }
        mMTCentralManager.setBluetoothChangedListener {
            Log.d("items", "bluetooth changed")
        }
    }

    fun initBlePermission() {
        var requestPermissionsList: Array<String>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissionsList = arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            requestPermissionsList = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        PermissionX.init(this)
            .permissions(requestPermissionsList.toList())
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    getString(R.string.need_permission_continue),
                    "Ok",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    getString(R.string.allow_permission_in_settings),
                    "Ok",
                    "Cancel"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    if (BLETool.isBluetoothTurnOn(this@ScanDeviceListActivity)) {
                        startScan()
                    } else {
                        BLETool.setBluetoothTurnOn(this@ScanDeviceListActivity)
                    }
                }
            }
    }

    private fun startScan() {
        Log.d("SCAN", "startScan")
        if (mMTCentralManager.isScanning()) {
            stopScan()
        }
        mMTCentralManager.clear()
        mMTCentralManager.startScan()
//        mDevicesListAdapter.submitList(null)
//        mObjectAnimator!!.start()
    }

    fun checkItems() {
        Log.d("items", mlist.toList().toString())
    }

    private fun stopScan() {
        Log.d("SCAN", "stopScan")
        if (mMTCentralManager.isScanning()) {
            mMTCentralManager.stopScan()
            mObjectAnimator!!.cancel()
        }
    }

    private fun connectedDevice(mtPeripheral: MTPeripheral) {
//        WaitDialog.show(R.string.loading)
        //        mMtCentralManager.connect(mtPeripheral,connectionStatueListener);
        mMTCentralManager.connect(mtPeripheral, object : ConnectionStatueListener {
            override fun onUpdateConnectionStatus(
                connectionStatus: ConnectionStatus,
                getPasswordListener: GetPasswordListener
            ) {
                runOnUiThread {
                    when (connectionStatus) {
                        ConnectionStatus.CONNECTING -> {
                            Log.e("tag", "CONNECTING")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "CONNECTING",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.CONNECTED -> {
                            Log.e("tag", "CONNECTED")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "CONNECTED",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        ConnectionStatus.READINGINFO -> {
                            Log.e("tag", "READINGINFO")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "READINGINFO",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.DEVICEVALIDATING -> {
                            Log.e("tag", "DEVICEVALIDATING")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "DEVICEVALIDATING",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.PASSWORDVALIDATING -> {
                            Log.e("tag", "PASSWORDVALIDATING")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "PASSWORDVALIDATING",
                                Toast.LENGTH_SHORT
                            ).show()
                            val password = "minew123"
                            getPasswordListener.getPassword(password)
                        }

                        ConnectionStatus.SYNCHRONIZINGTIME -> {
                            Log.e("tag", "SYNCHRONIZINGTIME")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "SYNCHRONIZINGTIME",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGCONNECTABLE -> {
                            Log.e("tag", "READINGCONNECTABLE")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "READINGCONNECTABLE",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGFEATURE -> {
                            Log.e("tag", "READINGFEATURE")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "READINGFEATURE",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGFRAMES -> {
                            Log.e("tag", "READINGFRAMES")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "READINGFRAMES",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGTRIGGERS -> {
                            Log.e("tag", "READINGTRIGGERS")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "READINGTRIGGERS",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.COMPLETED -> {
//                            WaitDialog.dismiss()
                            Log.e("tag", "COMPLETED")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "COMPLETED",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val intent = Intent()
//                            intent.setClass(
//                                this@ScanDeviceListActivity,
//                                DeviceConnectedActivity::class.java
//                            )
//                            startActivity(intent)
                        }

                        ConnectionStatus.CONNECTFAILED, ConnectionStatus.DISCONNECTED -> {
//                            WaitDialog.dismiss()
                            Log.e("tag", "DISCONNECTED")
                            Toast.makeText(
                                this@ScanDeviceListActivity,
                                "DISCONNECTED",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGSENSORS -> TODO()
                    }
                }
            }

            override fun onError(e: MTException) {
                Log.e("tag", e.message)
            }
        })
        Config.mConnectedMTPeripheral = mtPeripheral
    }

    override fun onDestroy() {
        super.onDestroy()
        mMTCentralManager.stopService()
        stopScan()
    }




}



