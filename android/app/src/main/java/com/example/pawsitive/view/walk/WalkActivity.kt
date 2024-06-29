package com.example.pawsitive.view.walk

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pawsitive.R
import com.example.pawsitive.util.SendGeolocationTask
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.example.pawsitive.view.walk.screens.OverlayWalk
import com.example.pawsitive.viewmodel.ApiViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.minew.beaconplus.sdk.MTCentralManager
import com.minew.beaconplus.sdk.MTPeripheral
import com.minew.beaconplus.sdk.Utils.BLETool
import com.minew.beaconplus.sdk.enums.ConnectionStatus
import com.minew.beaconplus.sdk.enums.FrameType
import com.minew.beaconplus.sdk.exception.MTException
import com.minew.beaconplus.sdk.interfaces.ConnectionStatueListener
import com.minew.beaconplus.sdk.interfaces.GetPasswordListener
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.config.Configuration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WalkActivity : AppCompatActivity() {
//    val mObjectAnimator: ObjectAnimator? = null

    private lateinit var mMTCentralManager: MTCentralManager
    private lateinit var task: SendGeolocationTask
    private lateinit var apiViewModel: ApiViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var id: String

    var latitude: Double = 0.0
    var longtitude: Double = 0.0

    val beaconViewModel by viewModel<BeaconViewModel>()

    private var onNavigateAction: (() -> Unit)? = null

    fun performNavigation() {
        onNavigateAction?.invoke()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("create", "on create")
        val apiViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ApiViewModel::class.java]
        this@WalkActivity.apiViewModel = apiViewModel


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 0)
        }
        initBleManager()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

        if (!mMTCentralManager.isScanning) {
            setBleManagerListener()
        }

        initBlePermission()
        val historyId = intent.extras?.getString("historyId")
        id = historyId!!

        task = SendGeolocationTask(apiViewModel, historyId)

        mMTCentralManager.startService()


//        enableEdgeToEdge()
        setContent {
            OverlayWalk(
                beaconViewModel,
                { refresh() },
                ::connect,
                ::disconnect,
                apiViewModel,
                historyId
            ) { navigateAction ->
                onNavigateAction = navigateAction
            }
        }
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = "Pawsitive"
    }

    fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) {
                    val location: Location? = it.result
                    if (location == null) {
                        Toast.makeText(this, "Pusta wartość", Toast.LENGTH_SHORT).show()
                    } else {
                        latitude = location.latitude
                        longtitude = location.longitude
                    }
                }
            } else {
                Toast.makeText(this, "Włącz lokalizację", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Udzielono", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(applicationContext, "Nie udzielono", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun initBleManager() {
        mMTCentralManager = MTCentralManager.getInstance(this)
        mMTCentralManager.startService()
    }

    fun setBleManagerListener() {
        mMTCentralManager.setMTCentralManagerListener { it ->

            val listToListen = mutableListOf<MTPeripheral>()
            for (beacon in it) {

                val connectionHandler = beacon.mMTConnectionHandler

                val frames = connectionHandler.allFrames
                for (frame in frames) {
                    val currSlot = frame.curSlot
                    if (currSlot == 5 && frame.frameType == FrameType.FrameTLM) {
                        listToListen.add(beacon)
                    } else {
                        if (currSlot == 5 && frame.frameType == FrameType.FrameNone) {
                            listToListen.remove(beacon)
                        }
                    }
                }
            }
            if (listToListen.isNotEmpty()) {
                getCurrentLocation()
                task.setGeolocation(latitude, longtitude)
                task.start()
            } else {
                task.stop()
            }
            beaconViewModel.setListenedList(listToListen)
            beaconViewModel.setBeaconList(it)
        }
//        mMTCentralManager.setBluetoothChangedListener {
//            Log.d("items", "bluetooth changed")
//        }
    }

    fun initBlePermission() {
        val requestPermissionsList: Array<String>

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
                    "Anuluj"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    getString(R.string.allow_permission_in_settings),
                    "Ok",
                    "Anuluj"
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
        if (mMTCentralManager.isScanning()) {
            stopScan()
        }
        mMTCentralManager.clear()
        mMTCentralManager.startScan()
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
                                "Łączenie",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.CONNECTED -> {
                            Log.e("tag", "CONNECTED")
                            Toast.makeText(
                                this@WalkActivity,
                                "Połączono",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGINFO -> {
                            Log.e("tag", "READINGINFO")
                            Toast.makeText(
                                this@WalkActivity,
                                "Odczyt danych",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.DEVICEVALIDATING -> {
                            Log.e("tag", "DEVICEVALIDATING")
                            Toast.makeText(
                                this@WalkActivity,
                                "Walidacja urządzenia",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.PASSWORDVALIDATING -> {
                            Log.e("tag", "PASSWORDVALIDATING")
                            Toast.makeText(
                                this@WalkActivity,
                                "Sprawdzanie hasła",
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
                                "Synchronizacja",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGCONNECTABLE -> {
                            Log.e("tag", "READINGCONNECTABLE")
                            Toast.makeText(
                                this@WalkActivity,
                                "Odczyt połączenia",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGFEATURE -> {
                            Log.e("tag", "READINGFEATURE")
                            Toast.makeText(
                                this@WalkActivity,
                                "Odczyt funkcjonalności",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGFRAMES -> {
                            Log.e("tag", "READINGFRAMES")
                            Toast.makeText(
                                this@WalkActivity,
                                "Odczyt klatek",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.READINGTRIGGERS -> {
                            Log.e("tag", "READINGTRIGGERS")
                            Toast.makeText(
                                this@WalkActivity,
                                "Odczyt wywołań",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ConnectionStatus.COMPLETED -> {
                            Log.e("tag", "COMPLETED")
                            Toast.makeText(
                                this@WalkActivity,
                                "Ukończono",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (!beaconViewModel.listenedDevices.contains(mtPeripheral)) {
                                beaconViewModel.setConnectedPeripheral(mtPeripheral)
                            }
                            performNavigation()
                        }

                        ConnectionStatus.CONNECTFAILED, ConnectionStatus.DISCONNECTED -> {
                            Log.e("tag", "DISCONNECTED")
                            beaconViewModel.connectedMTPeripheral = null
                            Toast.makeText(
                                this@WalkActivity,
                                "Rozłączono",
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
    }


    fun disconnect(mtPeripheral: MTPeripheral) {
        mMTCentralManager.disconnect(mtPeripheral)
    }

    override fun onDestroy() {
        super.onDestroy()


        runBlocking {
            val call: Call<String> = apiViewModel.walkService.cancelContract(id)
            call.enqueue(object : Callback<String> {
                override fun onResponse(
                    p0: Call<String>,
                    p1: Response<String>
                ) {
                }

                override fun onFailure(
                    p0: Call<String>,
                    p1: Throwable
                ) {
                }

            })
        }
    }
}
