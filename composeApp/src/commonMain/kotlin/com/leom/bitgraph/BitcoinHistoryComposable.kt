package com.leom.bitgraph

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        modifier = Modifier.fillMaxSize().padding(top = 64.dp).padding(16.dp),
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
        ) {
            Text(text = if (isLoading) "Loading..." else "Fetch Bitcoin History")
        }

        if (errorMessage != null) {
            Text(text = "Error: $errorMessage", color = androidx.compose.ui.graphics.Color.Red)
        }

        bitcoinHistory?.let { history ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(history) { priceHistory ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "Date: ${priceHistory.date}")
                        Text(text = "Price: $${priceHistory.price}")
                    }
                }
            }
        }
    }
}
