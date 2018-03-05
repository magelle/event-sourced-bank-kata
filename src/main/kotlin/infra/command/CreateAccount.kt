package infra.command

import domain.createAnAccount
import infra.AccountEventStore

class CreateAccount(private val idGenerator: () -> Int, private val accountEventStore: AccountEventStore) {

    fun execute() {
        createAnAccount(idGenerator)
                .let { accountEventStore.save(it) }
    }

}