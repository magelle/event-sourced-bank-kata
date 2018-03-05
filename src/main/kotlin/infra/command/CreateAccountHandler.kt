package infra.command

import domain.createAnAccount
import infra.AccountEventStore

class CreateAccountHandler(
        private val idGenerator: () -> Int,
        private val accountEventStore: AccountEventStore) : CommandHandler<CreateAccount> {

    override fun listenTo() = CreateAccount::class

    override fun handle(command: CreateAccount) {
        createAnAccount(idGenerator)
                .let { accountEventStore.save(it) }
    }

}