package dev.siro256.serverchecker

import dev.siro256.kotlin.consolelib.Console
import dev.siro256.serverchecker.data.DownAlertData
import dev.siro256.serverchecker.data.UpAlertData
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Webhook {
    private val client = HttpClient(CIO) {
        install(UserAgent) {
            agent = "ServerChecker/${ ServerChecker.VERSION } Webhook-Worker"
        }
    }

    fun sendDownAlert(data: DownAlertData) {
        post(
            """
            {
                "embeds": [
                    {
                        "title": "Service down alert",
                        "color": ${ 0xFF0000 },
                        "timestamp": "${ data.timeStamp }",
                        "footer": {
                            "text": "ServerChecker | https://github.com/Siro256/ServerChecker"
                        },
                        "fields": [
                            {
                                "name": "Name",
                                "value": "${ data.monitorTarget.name }",
                                "inline": false
                            },
                            {
                                "name": "Description",
                                "value": "${ data.monitorTarget.name }",
                                "inline": false
                            },
                            {
                                "name": "Detected time",
                                "value": "${ data.timeStamp }",
                                "inline": false
                            },
                            {
                                "name": "Target",
                                "value": "${ data.monitorTarget.destination }",
                                "inline": true
                            },
                            {
                                "name": "Monitor method",
                                "value": "${ data.monitorTarget.method }",
                                "inline": true
                            }
                        ]
                    }
                ] 
            }
        """.trimIndent()
        )
    }

    fun sendUpAlert(data: UpAlertData) {
        post(
            """
            {
                "embeds": [
                    {
                        "title": "Service up alert",
                        "color": ${ 0x00FF00 },
                        "timestamp": "${ data.timeStamp }",
                        "footer": {
                            "text": "ServerChecker | https://github.com/Siro256/ServerChecker"
                        },
                        "fields": [
                            {
                                "name": "Name",
                                "value": "${ data.monitorTarget.name }",
                                "inline": false
                            },
                            {
                                "name": "Description",
                                "value": "${ data.monitorTarget.name }",
                                "inline": false
                            },
                            {
                                "name": "Detected time",
                                "value": "${ data.timeStamp }",
                                "inline": false
                            },
                            {
                                "name": "Failed duration",
                                "value": "${ data.failedDuration }",
                                "inline": false
                            },
                            {
                                "name": "Target",
                                "value": "${ data.monitorTarget.destination }",
                                "inline": true
                            },
                            {
                                "name": "Monitor method",
                                "value": "${ data.monitorTarget.method }",
                                "inline": true
                            }
                        ]
                    }
                ] 
            }
        """.trimIndent()
        )
    }

    fun sendTestMessage() {
        post(
            """
            {
                "embeds": [
                    {
                        "title": "Test message",
                        "description": "ServerChecker can send message to the channel.",
                        "color": ${0x00FF00},
                        "timestamp": "${DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now())}",
                        "footer": {
                            "text": "ServerChecker | https://github.com/Siro256/ServerChecker"
                        }
                    }
                ]
            }
        """.trimIndent()
        )
    }

    private fun post(postData: String) {
        runBlocking {
            client.post<String>(ConfigManager.webhookUrl) {
                body = postData
                header("Content-Type", "application/json")
            }
        }
    }
}
