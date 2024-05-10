package com.example.pawsitive.view

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pawsitive.R
import com.example.pawsitive.navigation.MainScreen
import com.example.pawsitive.viewmodel.BeaconViewModel
import com.minew.beaconplus.sdk.MTCentralManager
import com.minew.beaconplus.sdk.MTFrameHandler
import com.minew.beaconplus.sdk.MTPeripheral
import com.minew.beaconplus.sdk.Utils.BLETool
import com.minew.beaconplus.sdk.enums.ConnectionStatus
import com.minew.beaconplus.sdk.enums.FrameType
import com.minew.beaconplus.sdk.exception.MTException
import com.minew.beaconplus.sdk.frames.IBeaconFrame
import com.minew.beaconplus.sdk.interfaces.ConnectionStatueListener
import com.minew.beaconplus.sdk.interfaces.GetPasswordListener
import com.permissionx.guolindev.PermissionX
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.config.Configuration


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()

        }
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = "Pawsitive"
    }



}


