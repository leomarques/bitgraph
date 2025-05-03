package com.leom.shared.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Common HTTP client configuration
 */
object HttpClientFactory {
    private val jsonConfig =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = false
            encodeDefaults = true
        }

    /**
     * Creates a configured HttpClient instance with:
     * - JSON serialization
     * - Request timeouts
     * - Logging
     * - Default headers
     *
     * Platform-specific engines are provided in platform modules.
     */
    fun create(): HttpClient {
        return HttpClient {
            // Install content negotiation for JSON serialization/deserialization
            install(ContentNegotiation) {
                json(jsonConfig)
            }

            // Configure request timeouts
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 15000
            }

            // Enable logging for development
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }

            // Add default headers to all requests
            defaultRequest {
                header("Accept", "application/json")
                header("Content-Type", "application/json")
            }
        }
    }
}
