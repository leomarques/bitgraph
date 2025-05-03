package com.leom.shared.di

import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * iOS-specific Koin initialization
 */
object KoinIOS {
    /**
     * Initialize Koin for iOS
     */
    fun init() {
        startKoin {
            modules(createPlatformModule())
        }
    }

    /**
     * Create platform-specific modules
     */
    private fun createPlatformModule() = module {
        // Register iOS-specific implementations
        single<ExampleRepository> { ExampleRepositoryImpl() }
    }
}