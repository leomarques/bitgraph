package com.leom.shared.di

import com.leom.shared.network.ApiService
import com.leom.shared.network.HttpClientFactory
import com.leom.shared.network.SampleRequestViewModel
import org.koin.dsl.module

/**
 * Koin DI module for networking components
 */
val networkModule =
    module {
        // HTTP client provided as a singleton
        single { HttpClientFactory.create() }

        // API service with injected HTTP client
        single { ApiService(get()) }

        // ViewModel for handling network operations
        factory { SampleRequestViewModel() }
    }
