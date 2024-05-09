package com.example.pawsitive.view

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.pawsitive.R
import com.example.pawsitive.navigation.LocalGlobalState

import com.example.pawsitive.navigation.MainScreen
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTCentralManager
import com.minew.beaconplus.sdk.MTPeripheral
import com.minew.beaconplus.sdk.Utils.BLETool
import com.permissionx.guolindev.PermissionX
import org.osmdroid.config.Configuration
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin


val LocalBeaconViewModel = compositionLocalOf<BeaconViewModel> { error("No ViewModel provided") }
class MainActivity : AppCompatActivity() {

    val mObjectAnimator: ObjectAnimator? = null

    lateinit var mMTCentralManager: MTCentralManager

    val beaconViewModel by viewModel<BeaconViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 0)
        }
        super.onCreate(savedInstanceState)
        initBleManager()
        setBleManagerListener()
        initBlePermission()
        setContent {
            CompositionLocalProvider(LocalBeaconViewModel provides beaconViewModel) {
                MainScreen()
            }
        }
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = "Pawsitive"
    }

    fun initBleManager() {
        mMTCentralManager = MTCentralManager.getInstance(this)
        mMTCentralManager.startService()
    }

    fun setBleManagerListener() {
        mMTCentralManager.setMTCentralManagerListener {
            Log.d("items", it.toString())
            beaconViewModel.setBeaconList(it.toMutableStateList())
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
                    if (BLETool.isBluetoothTurnOn(this@MainActivity)) {
                        startScan()
                    } else {
                        BLETool.setBluetoothTurnOn(this@MainActivity)
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
        Log.d("items", beaconViewModel.mlist.toList().toString())
    }

    private fun stopScan() {
        Log.d("SCAN", "stopScan")
        if (mMTCentralManager.isScanning()) {
            mMTCentralManager.stopScan()
            mObjectAnimator!!.cancel()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mMTCentralManager.stopService()
        stopScan()
    }
}


