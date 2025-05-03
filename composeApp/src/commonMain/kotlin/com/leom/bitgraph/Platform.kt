package com.leom.bitgraph

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
