package com.leom.shared.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Android-specific Koin initialization
 */
object KoinAndroid {
    /**
     * Initialize Koin for Android
     */
    fun init(context: Context) {
        startKoin {
            androidLogger()
            androidContext(context)
            modules(networkModule)
        }
    }

}