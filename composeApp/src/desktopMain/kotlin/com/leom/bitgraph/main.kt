package com.leom.bitgraph

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.leom.shared.di.KoinDesktop

fun main() {
    KoinDesktop.init()
    
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "bitgraph"
        ) {
            MainComposable()
        }
    }
}