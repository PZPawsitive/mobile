package com.example.pawsitive

import android.app.Application
import com.example.pawsitive.viewmodel.BeaconViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin() {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}

val appModule = module {
    viewModel { BeaconViewModel() }

}