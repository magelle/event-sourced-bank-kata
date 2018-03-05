package infra.command

import kotlin.reflect.KClass

interface CommandHandler<T: Command> {

    fun listenTo(): KClass<T>
    fun handle(command: T)

}