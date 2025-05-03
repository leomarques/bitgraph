package com.leom.bitgraph

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun MainComposable() {
    MaterialTheme {
        SampleRequestView(
            platform = getPlatform().name
        )
    }
}
