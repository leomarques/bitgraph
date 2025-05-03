package com.leom.shared.di

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
        single<ExampleRepository> { ExampleRepositoryImpl() }
    }
}