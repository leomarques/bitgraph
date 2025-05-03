package com.leom.bitgraph

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Desktop-specific Koin initialization
 */
object KoinDesktop {
    /**
     * Initialize Koin for Desktop
     */
    fun init() {
        startKoin {
            modules(createPlatformModule())
        }
    }

    /**
     * Create platform-specific modules
     */
    private fun createPlatformModule(): Module = module {
        // Register desktop-specific implementations
        // Example: single<SomeRepository> { SomeRepositoryImpl() }
    }
}