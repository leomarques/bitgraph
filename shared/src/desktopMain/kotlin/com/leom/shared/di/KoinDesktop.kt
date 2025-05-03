package com.leom.shared.di

import org.koin.core.context.startKoin

/**
 * Desktop-specific Koin initialization
 */
object KoinDesktop {
    /**
     * Initialize Koin for Desktop
     */
    fun init() {
        startKoin {
            modules(networkModule)
        }
    }
}
