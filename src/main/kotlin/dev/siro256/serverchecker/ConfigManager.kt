package dev.siro256.serverchecker

import dev.siro256.kotlin.consolelib.Console
import dev.siro256.serverchecker.data.MonitorTarget
import org.yaml.snakeyaml.Yaml
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

object ConfigManager {
    var webhookUrl = ""
        private set

    var useServer = true
        private set

    var bindAddress = "0.0.0.0"
        private set

    var bindPort = 6712
        private set

    var timeout = 2000
        private set

    var maxRetry = 3
        private set

    val target = mutableListOf<MonitorTarget>()

    fun deployFile() {
        val destinationFile = Paths.get("./config.yml")

        if (destinationFile.toFile().exists()) {
            val inputStream = destinationFile.toFile().inputStream()
            if ((Yaml().loadAs(inputStream, HashMap<String, Any>()::class.java)["configVersion"] as Int) == 1) {
                inputStream.close()
                return
            }
            inputStream.close()

            Console.println(
                """
                Detected another version ov the config.
                Old config file is rename to config.yml.old.
                Please setup config.yml.new and rename config.yml.
            """.trimIndent()
            )

            Files.move(destinationFile, Paths.get("./config.yml.old"))
            Files.copy(
                ClassLoader.getSystemResourceAsStream("config.yml")!!,
                Paths.get("./config.yml.new")
            )
            exitProcess(0)
        }

        Console.println(
            """
            Create config.yml in current directory.
            Please setup to your environment. 
            """.trimIndent()
        )

        Files.copy(ClassLoader.getSystemResourceAsStream("config.yml")!!, destinationFile)
        exitProcess(0)
    }

    fun loadConfig() {
        val yaml = Yaml()

        yaml.loadAs(Paths.get("./config.yml").toFile().inputStream(), HashMap<String, Any>()::class.java)
            .forEach { (key, value) ->
                when (key) {
                    "webhookUrl" -> webhookUrl = value.toString()
                    "useServer" -> useServer = value as Boolean
                    "bindAddress" -> bindAddress = value.toString()
                    "bindPort" -> bindPort = value as Int
                    "timeout" -> timeout = value as Int
                    "maxRetry" -> maxRetry = value as Int
                    "monitorTarget" -> {
                        yaml.loadAs(yaml.dump(value), mutableListOf<Map<String, Any>>()::class.java).forEach {
                            target.add(
                                MonitorTarget(
                                    name = it["name"]!!.toString(),
                                    description = it["description"]!!.toString()
                                        .let { description -> description.ifEmpty { "(Empty)" } },
                                    method = when (it["method"]!!.toString()) {
                                        "HTTP" -> MonitorMethod.HTTP
                                        "PING" -> MonitorMethod.PING
                                        else -> MonitorMethod.SERVER_CHECKER
                                    },
                                    destination = it["destination"]!!.toString(),
                                    period = it["period"]!! as Int
                                )
                            )
                        }
                    }
                }
            }
    }
}
