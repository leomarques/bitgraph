package com.leom.shared.di

import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Main Koin module for the shared module
 */
val sharedModule = module {
    // Add your dependencies here
    // Example: 
    // single<SomeRepository> { SomeRepositoryImpl() }
    // factory { SomeUseCase(get()) }
}

/**
 * List of all modules in the application
 */
val appModules = listOf(sharedModule)

/**
 * Initialize Koin for Dependency Injection
 */
fun initKoin() {
    startKoin {
        modules(appModules)
    }
}