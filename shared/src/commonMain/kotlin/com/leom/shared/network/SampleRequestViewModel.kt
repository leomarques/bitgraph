package com.leom.shared.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * ViewModel for network operations
 */
class SampleRequestViewModel : KoinComponent {
    private val apiService: ApiService by inject()
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // UI state representing the current network operation state
    private val _uiState = MutableStateFlow<NetworkUiState>(NetworkUiState.Idle)
    val uiState: StateFlow<NetworkUiState> = _uiState

    /**
     * Fetch data from the API
     */
    fun fetchData() {
        _uiState.update { NetworkUiState.Loading }

        coroutineScope.launch {
            apiService.fetchSampleData()
                .catch { error ->
                    _uiState.update {
                        NetworkUiState.Error(
                            error.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collectLatest { result ->
                    result.fold(
                        onSuccess = { response ->
                            _uiState.update {
                                NetworkUiState.Success("Fetched ${response.results.size} items, total count: ${response.totalCount}")
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                NetworkUiState.Error(error.message ?: "Unknown error occurred")
                            }
                        }
                    )
                }
        }
    }

    /**
     * Send data to the API
     */
    fun sendData(query: String) {
        if (query.isBlank()) {
            _uiState.update { NetworkUiState.Error("Query cannot be empty") }
            return
        }

        _uiState.update { NetworkUiState.Loading }

        coroutineScope.launch {
            val request = SampleRequest(query = query)

            apiService.postData(request)
                .catch { error ->
                    _uiState.update {
                        NetworkUiState.Error(
                            error.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collectLatest { result ->
                    result.fold(
                        onSuccess = { response ->
                            _uiState.update {
                                NetworkUiState.Success("Data sent successfully, received ID: ${response.id}")
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                NetworkUiState.Error(error.message ?: "Unknown error occurred")
                            }
                        }
                    )
                }
        }
    }
}

/**
 * UI state representing the current state of network operations
 */
sealed class NetworkUiState {
    data object Idle : NetworkUiState()
    data object Loading : NetworkUiState()
    data class Success(val message: String) : NetworkUiState()
    data class Error(val message: String) : NetworkUiState()
}