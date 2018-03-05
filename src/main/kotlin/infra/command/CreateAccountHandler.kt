package infra.command

import domain.createAnAccount
import infra.AccountEventStore
import kotlin.reflect.KClass

class CreateAccountHandler(
        private val idGenerator: () -> Int,
        private val accountEventStore: AccountEventStore) : CommandHandler<CreateAccount> {

    override fun listenTo() = CreateAccount::class as KClass<Command>

    override fun handle(command: CreateAccount) {
        createAnAccount(idGenerator)
                .let { accountEventStore.save(it) }
    }

}