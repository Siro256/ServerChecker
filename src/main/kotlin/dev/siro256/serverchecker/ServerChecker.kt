package dev.siro256.serverchecker

import dev.siro256.kotlin.consolelib.Console
import dev.siro256.kotlin.eventlib.EventManager
import dev.siro256.serverchecker._enum.TargetStatus
import dev.siro256.serverchecker.data.DownAlertData
import dev.siro256.serverchecker.data.UpAlertData
import dev.siro256.serverchecker.worker.HttpWorker
import dev.siro256.serverchecker.worker.MonitorWorker
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

object ServerChecker {
    const val VERSION = "1.0.0-SNAPSHOT"

    private val monitorWorker = mutableListOf<MonitorWorker>()
    val monitorTask = mutableListOf<Timer>()

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

        //Register task
        registerTask()

        while (true) {
            Thread.sleep(1000)
        }
    }

    private fun registerTask() {
        ConfigManager.target.forEach { target ->
            val worker = monitorWorker.first { it.method == target.method }

            monitorTask.add(
                Timer(target.name, true).apply {
                    schedule(
                        object : TimerTask() {
                            override fun run() {
                                val status = worker.checkTarget(target)
                                if (status) { //Current: UP
                                    when (target.previousStatus) {
                                        TargetStatus.UP -> return
                                        else -> {
                                            val time = ZonedDateTime.now()

                                            Webhook.sendUpAlert(
                                                UpAlertData(
                                                    DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(time),
                                                    if (target.previousStatus == TargetStatus.UNCONFIRMED) {
                                                        "Start-up"
                                                    } else {
                                                        DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")
                                                            .format(target.lastChangedTime.minus(time.toInstant().toEpochMilli(), ChronoUnit.MILLIS))
                                                    },
                                                    target
                                                )
                                            )

                                            target.previousStatus = TargetStatus.UP
                                            target.lastChangedTime = time
                                        }
                                    }
                                } else { //Current: DOWN
                                    when (target.previousStatus) {
                                        TargetStatus.DOWN -> return
                                        else -> {
                                            val time = ZonedDateTime.now()

                                            Webhook.sendDownAlert(DownAlertData(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(time), target))

                                            target.previousStatus = TargetStatus.DOWN
                                            target.lastChangedTime = time
                                        }
                                    }
                                }
                            }
                        },
                        0L,
                        target.period * 1000L
                    )
                }
            )
        }
    }
}
