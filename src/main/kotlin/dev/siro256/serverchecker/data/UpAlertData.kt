package dev.siro256.serverchecker.data

data class UpAlertData(
    val timeStamp: String,
    val failedDuration: String,
    val monitorTarget: MonitorTarget
)
