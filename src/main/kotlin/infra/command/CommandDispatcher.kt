package infra.command

class CommandDispatcher(handlers: List<CommandHandler<in Command>>) {

    private val handlerByCommand = handlers.map { it.listenTo() to it }.toMap()

    fun dispatch(command: Command) {
        handlerByCommand[command::class]?.handle(command)
    }

}