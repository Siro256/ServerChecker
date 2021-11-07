package dev.siro256.serverchecker.command

import dev.siro256.kotlin.consolelib.Console
import dev.siro256.serverchecker.ServerChecker
import kotlin.system.exitProcess

object StopCommand: Command {
    override fun execute(commandName: String, args: List<String>) {
        ServerChecker.monitorTask.forEach {
            it.cancel()
        }

        Console.println("Stop.")
        exitProcess(0)
    }

}
