package com.example.pawsitive.view

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pawsitive.R

import com.example.pawsitive.navigation.MainScreen
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTCentralManager
import com.minew.beaconplus.sdk.MTFrameHandler
import com.minew.beaconplus.sdk.Utils.BLETool
import com.minew.beaconplus.sdk.enums.FrameType
import com.minew.beaconplus.sdk.frames.IBeaconFrame
import com.permissionx.guolindev.PermissionX
import org.osmdroid.config.Configuration
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val mObjectAnimator: ObjectAnimator? = null

    lateinit var mMTCentralManager: MTCentralManager

    val beaconViewModel by viewModel<BeaconViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 0)
        }
        super.onCreate(savedInstanceState)
        initBleManager()
        setBleManagerListener()
        initBlePermission()
        setContent {
            MainScreen(beaconViewModel, { refresh() })

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
        mMTCentralManager.setMTCentralManagerListener { it ->
            for (beacon in it) {
                val mtFrameHandler: MTFrameHandler = beacon.mMTFrameHandler
                val mac = mtFrameHandler.mac
                val name = mtFrameHandler.name
                val battery = mtFrameHandler.battery
                val rssi = mtFrameHandler.rssi
                val frames = mtFrameHandler.advFrames
                for (frame in frames) {
                    val frametype = frame.frameType
                    if (frametype == FrameType.FrameiBeacon) {
                        val tlmFrame: IBeaconFrame = frame as IBeaconFrame
                        Log.v(
                            "beaconplus",
                            tlmFrame.battery.toString()
                        );
                    }

                    Log.d("beaconData", "frametype: $frametype")
                }
                Log.d("beaconData", "$mac, $name, $battery, $rssi")
            }
            beaconViewModel.setBeaconList(it)
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

    private fun stopScan() {
        Log.d("SCAN", "stopScan")
        if (mMTCentralManager.isScanning()) {
            mMTCentralManager.stopScan()
            mObjectAnimator!!.cancel()
        }
    }

    fun refresh() {
        if (mMTCentralManager.isScanning) {
            mMTCentralManager.stopScan()
            mMTCentralManager.clear()
            mMTCentralManager.startScan()
        }
        else {
            mMTCentralManager.clear()
            mMTCentralManager.startScan()
        }
    }

    fun connect() {
        TODO()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMTCentralManager.stopService()
        stopScan()
    }

}


