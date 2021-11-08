package dev.siro256.serverchecker.worker

import dev.siro256.serverchecker.ConfigManager
import dev.siro256.serverchecker._enum.MonitorMethod
import dev.siro256.serverchecker.ServerChecker
import dev.siro256.serverchecker.data.MonitorTarget
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

object HttpWorker: MonitorWorker() {
    override val method = MonitorMethod.HTTP

    private val client = HttpClient(CIO) {
        expectSuccess = false

        engine {
            requestTimeout = ConfigManager.timeout.toLong()
        }

        install(UserAgent) {
            agent = "ServerChecker/${ ServerChecker.VERSION } HTTP-Worker"
        }
    }

    override fun checkTarget(target: MonitorTarget): Boolean  {
        if (target.method != PingWorker.method) throw IllegalArgumentException("Target method must be ${ServerCheckerWorker.method.name}.")

        return runBlocking {
            if ((client.get(target.destination) as HttpResponse).status != HttpStatusCode.OK)
                return@runBlocking false else true
        }
    }
}
