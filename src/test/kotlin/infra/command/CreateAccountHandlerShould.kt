package infra.command

import domain.AccountCreated
import infra.AccountEventStore
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAccountHandlerShould {

    private val accountId = 1

    @Test
    fun `create an account`() {
        val accountEventStore = AccountEventStore()
        val createAccountHandler = CreateAccountHandler({ accountId }, accountEventStore)
        createAccountHandler.handle(CreateAccount())
        assertEquals(listOf(AccountCreated(accountId)), accountEventStore.eventsOf(accountId))
    }

}