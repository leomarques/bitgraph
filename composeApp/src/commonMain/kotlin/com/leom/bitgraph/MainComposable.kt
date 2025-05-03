package com.leom.bitgraph

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.leom.shared.network.ApiService
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun MainComposable() {
    MaterialTheme {
        BitcoinHistoryComposable(
            apiService = koinInject<ApiService>(),
        )
    }
}
