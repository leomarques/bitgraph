package com.leom.bitgraph

import android.app.Application
import com.leom.shared.di.KoinAndroid

/**
 * Main application class that initializes dependencies
 */
class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin
        KoinAndroid.init(this)
    }
}