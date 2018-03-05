package infra.command

import kotlin.reflect.KClass

class CommandDispatcher(private val handlerByCommand: Map<KClass<out Command>, CommandHandler<in Command>>) {

    fun dispatch(command: Command) {
        handlerByCommand[command::class]?.handle(command)
    }

}