package com.poncegl.sigc.core.util

interface NetworkMonitor {
    fun isOnline(): Boolean
}

// Implementación Mock para el MVP (siempre hay internet)
class MockNetworkMonitor : NetworkMonitor {
    override fun isOnline(): Boolean = true
}