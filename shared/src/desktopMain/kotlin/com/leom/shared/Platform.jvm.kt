package com.leom.shared

actual fun platform(): String {
    return "JVM Desktop (${System.getProperty("os.name")})"
}
