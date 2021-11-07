package dev.siro256.serverchecker.worker

import dev.siro256.serverchecker._enum.MonitorMethod
import dev.siro256.serverchecker.data.MonitorTarget

abstract class MonitorWorker {
    abstract val method: MonitorMethod

    @Throws(IllegalArgumentException::class)
    abstract fun checkTarget(target: MonitorTarget): Boolean

    override fun toString(): String {
        return "$method-Worker"
    }
}
