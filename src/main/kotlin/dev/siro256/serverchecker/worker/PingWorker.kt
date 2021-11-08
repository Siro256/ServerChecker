package dev.siro256.serverchecker.worker

import dev.siro256.serverchecker.ConfigManager
import dev.siro256.serverchecker._enum.MonitorMethod
import dev.siro256.serverchecker.data.MonitorTarget
import java.net.InetAddress

object PingWorker: MonitorWorker() {
    override val method = MonitorMethod.PING

    override fun checkTarget(target: MonitorTarget): Boolean {
        if (target.method != method) throw IllegalArgumentException("Target method must be PING.")

        return InetAddress.getByName(target.destination).isReachable(ConfigManager.timeout)
    }
}
