package dev.siro256.serverchecker

import dev.siro256.kotlin.consolelib.Console

object ServerChecker {
    const val VERSION = "1.0.0-SNAPSHOT"

    @JvmStatic
    fun main(args: Array<String>) {
        //Initialize console class
        Console.initialize()
    }
}
