package com.leom.bitgraph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.leom.shared.network.ApiService
import com.leom.shared.network.BitcoinPriceHistory
import kotlinx.coroutines.launch

@Composable
fun BitcoinHistoryComposable(apiService: ApiService) {
    val scope = rememberCoroutineScope()
    var bitcoinHistory by remember { mutableStateOf<List<BitcoinPriceHistory>?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onSurface)
            .windowInsetsPadding(WindowInsets.safeDrawing).padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    try {
                        apiService.fetchBitcoinHistory().collect { result ->
                            if (result.isSuccess) {
                                bitcoinHistory = result.getOrNull()
                            } else {
                                errorMessage = result.exceptionOrNull()?.message
                            }
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFd7592e),
                contentColor = White
            )
        ) {
            Text(text = if (isLoading) "Loading..." else "Fetch Bitcoin History")
        }

        if (errorMessage != null) {
            Text(text = "Error: $errorMessage", color = Color.Red)
        }

        bitcoinHistory?.let { history ->
            Chart(history)
        }
    }
}
