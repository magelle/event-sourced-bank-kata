package infra.command

import domain.createAnAccount
import infra.AccountEventStore

class CreateAccount(private val accountEventStore: AccountEventStore, private val idGenerator: () -> Int) {

    fun execute() {
        createAnAccount(idGenerator)
                .let { accountEventStore.save(it) }
    }

}