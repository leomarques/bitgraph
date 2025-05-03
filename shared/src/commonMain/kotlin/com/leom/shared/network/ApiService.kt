package com.leom.shared.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Example API service for making HTTP requests
 */
class ApiService(private val httpClient: HttpClient) {
    companion object {
        private const val BASE_URL = "https://api.coingecko.com/api/v3"
    }

    /**
     * Fetches the historical price of Bitcoin for the last 365 days.
     * @return Flow emitting the API response
     */
    fun fetchBitcoinHistory(): Flow<Result<List<BitcoinPriceHistory>>> =
        flow {
            try {
                val response =
                    httpClient.get("$BASE_URL/coins/bitcoin/market_chart") {
                        url {
                            parameters.append("vs_currency", "usd")
                            parameters.append("days", "365")
                        }
                        contentType(ContentType.Application.Json)
                    }

                // Log the raw response for debugging purposes
                val rawJson = response.body<String>()
                println("Raw Response: $rawJson")

                // Configure JSON with ignoreUnknownKeys true
                val json = Json { ignoreUnknownKeys = true }
                val marketData: BitcoinMarketData = json.decodeFromString(rawJson)
                val history = marketData.prices.map { BitcoinPriceHistory(date = it[0], price = it[1]) }

                emit(Result.success(history))
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unknown error occurred"
                println("Error fetching Bitcoin history: $errorMessage")
                emit(Result.failure(Exception("Failed to fetch Bitcoin history: $errorMessage")))
            }
        }
}

/**
 * Updated data models for the Bitcoin price history API response
 */
@Serializable
data class BitcoinMarketData(
    val prices: List<List<Double>>,
)

@Serializable
data class BitcoinPriceHistory(
    val date: Double, // Timestamp in milliseconds
    val price: Double, // Price in USD
)
