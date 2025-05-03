package com.leom.bitgraph

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    // Initialize Koin for Desktop
    KoinDesktop.init()
    
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "bitgraph"
        ) {
            App()
        }
    }
}