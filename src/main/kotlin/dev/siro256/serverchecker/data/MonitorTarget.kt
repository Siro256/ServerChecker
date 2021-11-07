package dev.siro256.serverchecker.data

import dev.siro256.serverchecker._enum.MonitorMethod

data class MonitorTarget(
    val name: String,
    val description: String,
    val destination: String,
    val method: MonitorMethod,
    val period: Int
)
