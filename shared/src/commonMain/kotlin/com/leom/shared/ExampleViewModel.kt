package com.leom.shared

import com.leom.shared.di.ExampleRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Example ViewModel that demonstrates Koin dependency injection
 */
class ExampleViewModel : KoinComponent {
    // Lazy inject repository
    private val repository: ExampleRepository by inject()

    /**
     * Get data from the repository
     */
    fun getData(): String {
        return repository.getData()
    }
}