package dev.siro256.serverchecker.command

interface Command {
    fun execute(commandName: String, args: List<String>)
}
