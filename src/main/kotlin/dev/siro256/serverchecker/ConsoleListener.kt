package dev.siro256.serverchecker

import dev.siro256.kotlin.consolelib.ConsoleInputEvent
import dev.siro256.kotlin.eventlib.EventHandler

object ConsoleListener {
    @EventHandler
    fun onConsoleInput(event: ConsoleInputEvent) {
        val splitted = event.input.split(" ")

        splitted.first().let {
            when {
            }
        }
    }
}
