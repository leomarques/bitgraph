package com.leom.shared.di

/**
 * Simple example of using Koin in a KMP project
 */
class KoinHelper {
    companion object {
        /**
         * Initialize Koin modules
         */
        fun initKoin() {
            // This will be initialized in the platform-specific implementations
        }
    }
}

/**
 * Example repository to demonstrate Koin injection
 */
interface ExampleRepository {
    fun getData(): String
}

/**
 * Example implementation of the repository
 */
class ExampleRepositoryImpl : ExampleRepository {
    override fun getData(): String {
        return "Data from repository"
    }
}