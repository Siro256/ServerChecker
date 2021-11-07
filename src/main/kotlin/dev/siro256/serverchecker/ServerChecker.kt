package dev.siro256.serverchecker

import dev.siro256.kotlin.consolelib.Console
import dev.siro256.kotlin.eventlib.EventManager
import dev.siro256.serverchecker.worker.HttpWorker
import dev.siro256.serverchecker.worker.MonitorWorker

object ServerChecker {
    const val VERSION = "1.0.0-SNAPSHOT"

    private val monitorWorker = mutableListOf<MonitorWorker>()
    @JvmStatic
    fun main(args: Array<String>) {
        //Initialize console class
        Console.initialize()

        //Initialize config
        ConfigManager.deployFile()
        ConfigManager.loadConfig()

        //Register console event
        EventManager.registerHandler(ConsoleListener)

        //Register worker to list
        listOf(
            HttpWorker
        ).forEach { monitorWorker.add(it) }
    }
}
