package infra.command

import kotlin.reflect.KClass

interface CommandHandler<in T: Command> {

    fun listenTo(): KClass<Command>
    fun handle(command: T)

}