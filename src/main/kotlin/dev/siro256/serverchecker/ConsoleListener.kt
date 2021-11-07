package dev.siro256.serverchecker

import dev.siro256.kotlin.consolelib.ConsoleInputEvent
import dev.siro256.kotlin.eventlib.EventHandler
import dev.siro256.serverchecker.command.StopCommand

object ConsoleListener {
    @EventHandler
    fun onConsoleInput(event: ConsoleInputEvent) {
        val splitted = event.input.split(" ")

        splitted.first().let {
            when {
                it.equals("stop", true) -> StopCommand.execute(it, splitted.drop(1))
            }
        }
    }
}
