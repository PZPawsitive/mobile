package com.example.pawsitive.view

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pawsitive.R
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.example.pawsitive.walkactivityviews.OverlayScreen
import com.minew.beaconplus.sdk.MTCentralManager
import com.minew.beaconplus.sdk.MTFrameHandler
import com.minew.beaconplus.sdk.MTPeripheral
import com.minew.beaconplus.sdk.Utils.BLETool
import com.minew.beaconplus.sdk.enums.ConnectionStatus
import com.minew.beaconplus.sdk.enums.FrameType
import com.minew.beaconplus.sdk.exception.MTException
import com.minew.beaconplus.sdk.interfaces.ConnectionStatueListener
import com.minew.beaconplus.sdk.interfaces.GetPasswordListener
import com.permissionx.guolindev.PermissionX
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.config.Configuration



class WalkActivity : AppCompatActivity() {
    val mObjectAnimator: ObjectAnimator? = null

    lateinit var mMTCentralManager: MTCentralManager

    val beaconViewModel by viewModel<BeaconViewModel>()
    private var onNavigateAction: (() -> Unit)? = null

    lateinit var task: SendGeolocationTask

    fun performNavigation() {
        onNavigateAction?.invoke()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 0)
        }
        initBleManager()
        setBleManagerListener()
        initBlePermission()
        task = SendGeolocationTask()
        mMTCentralManager.startService()
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {

//            Button(onClick = {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            }) {
//
//                Text(text = "back")
//            }
            OverlayScreen(
                beaconViewModel,
                { refresh() },
                ::connect,
                ::disconnect
            ) { navigateAction ->
                onNavigateAction = navigateAction
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
        var i = 0
        mMTCentralManager.setMTCentralManagerListener { it ->
            Log.d("trigger", it.toString())
            for (beacon in it) {
                val connectionHandler = beacon.mMTConnectionHandler
                val mtFrameHandler: MTFrameHandler = beacon.mMTFrameHandler
                val frames = connectionHandler.allFrames
                val c = mtFrameHandler.advFrames
                var count = 0
//                c.forEach {
//                    Log.d("result", "currslot: ${it}")
//                    val smth = it.frameType
//                    Log.d("result", "frame: ${smth} count: $count")
//                    count += 1
//                }
                for (frame in frames) {
                    val currSlot = frame.curSlot
                    if (currSlot == 5 && frame.frameType == FrameType.FrameTLM) {
//                        Log.d("result", "result!!! $i")
//                        i += 1
                        beaconViewModel.setListenedList(it)
                        task.start() // broken
                    }
                    else {
                        if (currSlot == 5 && frame.frameType == FrameType.FrameNone) {
                            beaconViewModel.setListenedList(emptyList())
                            task.stop() // broken
                        }
                    }
                }
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
                    if (BLETool.isBluetoothTurnOn(this@WalkActivity)) {
                        startScan()
                    } else {
                        BLETool.setBluetoothTurnOn(this@WalkActivity)
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
//            mObjectAnimator!!.cancel()
        }
    }

    fun refresh() {
        if (mMTCentralManager.isScanning) {
            mMTCentralManager.stopScan()
            mMTCentralManager.clear()
            mMTCentralManager.startScan()
        } else {
            mMTCentralManager.clear()
            mMTCentralManager.startScan()
        }
    }

    fun connect(mtPeripheral: MTPeripheral) {
        mMTCentralManager.connect(mtPeripheral, object : ConnectionStatueListener {
            override fun onUpdateConnectionStatus(
                connectionStatus: ConnectionStatus,
                getPasswordListener: GetPasswordListener?
            ) {
                Log.d("tag", "thread")

                runOnUiThread {
                    when (connectionStatus) {
                        ConnectionStatus.CONNECTING -> {
                            Log.e("tag", "CONNECTING")
                            Toast.makeText(
                                this@WalkActivity,
                                "CONNECTING",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.CONNECTED -> {
                            Log.e("tag", "CONNECTED")
                            Toast.makeText(
                                this@WalkActivity,
                                "CONNECTED",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGINFO -> {
                            Log.e("tag", "READINGINFO")
                            Toast.makeText(
                                this@WalkActivity,
                                "READINGINFO",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.DEVICEVALIDATING -> {
                            Log.e("tag", "DEVICEVALIDATING")
                            Toast.makeText(
                                this@WalkActivity,
                                "DEVICEVALIDATING",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.PASSWORDVALIDATING -> {
                            Log.e("tag", "PASSWORDVALIDATING")
                            Toast.makeText(
                                this@WalkActivity,
                                "PASSWORDVALIDATING",
                                Toast.LENGTH_SHORT
                            ).show()
                            val password = "minew123"
                            if (getPasswordListener != null) {
                                getPasswordListener.getPassword(password)
                            }
                        }

                        ConnectionStatus.SYNCHRONIZINGTIME -> {
                            Log.e("tag", "SYNCHRONIZINGTIME")
                            Toast.makeText(
                                this@WalkActivity,
                                "SYNCHRONIZINGTIME",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGCONNECTABLE -> {
                            Log.e("tag", "READINGCONNECTABLE")
                            Toast.makeText(
                                this@WalkActivity,
                                "READINGCONNECTABLE",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGFEATURE -> {
                            Log.e("tag", "READINGFEATURE")
                            Toast.makeText(
                                this@WalkActivity,
                                "READINGFEATURE",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGFRAMES -> {
                            Log.e("tag", "READINGFRAMES")
                            Toast.makeText(
                                this@WalkActivity,
                                "READINGFRAMES",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGTRIGGERS -> {
                            Log.e("tag", "READINGTRIGGERS")
                            Toast.makeText(
                                this@WalkActivity,
                                "READINGTRIGGERS",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.COMPLETED -> {
                            Log.e("tag", "COMPLETED")
                            Toast.makeText(
                                this@WalkActivity,
                                "COMPLETED",
                                Toast.LENGTH_SHORT
                            ).show()

                            mtPeripheral.mMTConnectionHandler.resetFactorySetting { s, _ ->
                                Log.d("beaconplus", "resetted")
                            }
                            mtPeripheral.mMTConnectionHandler.mTConnectionFeature
                            beaconViewModel.setConnectedPeripheral(mtPeripheral)

                            performNavigation()
//                            val intent = Intent()
//                            intent.setClass(
//                                this@MainActivity,
//                                DeviceConnectedActivity::class.java
//                            )
//                            startActivity(intent)
                        }

                        ConnectionStatus.CONNECTFAILED, ConnectionStatus.DISCONNECTED -> {
                            Log.e("tag", "DISCONNECTED")
                            beaconViewModel.connectedMTPeripheral = null
                            Toast.makeText(
                                this@WalkActivity,
                                "DISCONNECTED",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGSENSORS -> {
                            Log.d("tag", "reading sensors")
                        }
                    }
                }
            }

            override fun onError(e: MTException) {
                Log.e("tag", e.message)
            }
        })
//        Config.mConnectedMTPeripheral = mtPeripheral
    }

    fun navigateToDeviceConnected(action: () -> Unit) {
        action()
    }

    fun disconnect(mtPeripheral: MTPeripheral) {
        mMTCentralManager.disconnect(mtPeripheral)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMTCentralManager.stopService()
        stopScan()
    }
}
