package com.leom.bitgraph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import com.leom.shared.network.BitcoinPriceHistory
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.Zoom
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.fill
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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

    val label = rememberTextComponent(
        style = TextStyle(
            color = White
        )
    )

    CartesianChartHost(
        modifier = Modifier.fillMaxSize(),
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(fill(Color(0xFFd7592e)))
                    )
                )
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = priceFormatter,
                label = label
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = dateFormatter,
                label = label
            ),
        ),
        modelProducer = modelProducer,
        zoomState = zoomState
    )
}

private fun formatDate(timestamp: Long): String {
    val date = Instant.fromEpochMilliseconds(timestamp)
    val dateTime = TimeZone.currentSystemDefault().let { timeZone ->
        date.toLocalDateTime(timeZone)
    }

    return "${dateTime.monthNumber}/${dateTime.year % 100}"
}
