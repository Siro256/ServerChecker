package dev.siro256.serverchecker.data

import dev.siro256.serverchecker._enum.MonitorMethod
import dev.siro256.serverchecker._enum.TargetStatus
import java.time.ZonedDateTime

data class MonitorTarget(
    val name: String,
    val description: String,
    val destination: String,
    val method: MonitorMethod,
    val period: Int,
    var previousStatus: TargetStatus = TargetStatus.UNCONFIRMED,
    var lastChangedTime: ZonedDateTime = ZonedDateTime.now()
)
