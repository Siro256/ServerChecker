package dev.siro256.serverchecker

import dev.siro256.kotlin.consolelib.Console
import org.yaml.snakeyaml.Yaml
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

object ConfigManager {
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

}
