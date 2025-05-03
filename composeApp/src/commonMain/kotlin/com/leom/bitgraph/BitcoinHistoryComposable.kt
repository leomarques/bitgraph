package com.leom.bitgraph

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import com.leom.shared.network.ApiService
import com.leom.shared.network.BitcoinPriceHistory
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.Zoom
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import kotlinx.coroutines.launch
import kotlinx.datetime.*

@Composable
fun BitcoinHistoryComposable(apiService: ApiService) {
    val scope = rememberCoroutineScope()
    var bitcoinHistory by remember { mutableStateOf<List<BitcoinPriceHistory>?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing),
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
            Chart(history)
        }
    }
}

@Composable
fun Chart(history: List<BitcoinPriceHistory>) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val zoomState = rememberVicoZoomState(
        zoomEnabled = true,
        initialZoom = Zoom.Content
    )

    val priceFormatter = CartesianValueFormatter { _, value, _ ->
        "$${value.toInt()}"
    }

    val dateFormatter = CartesianValueFormatter { _, value, _ ->
        val index = value.toInt()
        if (index >= 0 && index < history.size) {
            val timestamp = history[index].date.toLong()
            formatDate(timestamp)
        } else ""
    }

    LaunchedEffect(history) {
        modelProducer.runTransaction {
            lineSeries {
                series(history.map { it.price.toFloat() }.toList())
            }
        }
    }

    CartesianChartHost(
        modifier = Modifier.fillMaxSize(),
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = priceFormatter
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = dateFormatter
            ),
        ),
        modelProducer = modelProducer,
        zoomState = zoomState
    )
}

fun formatDate(timestamp: Long): String {
    val date = Instant.fromEpochMilliseconds(timestamp)
    val dateTime = TimeZone.currentSystemDefault().let { timeZone ->
        date.toLocalDateTime(timeZone)
    }

    return "${dateTime.monthNumber}/${dateTime.dayOfMonth}"
}
