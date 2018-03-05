package infra.command

import domain.AccountCreated
import infra.AccountEventStore
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAccountShould {

    private val accountId = 1

    @Test
    fun `create an account`() {
        val accountEventStore = AccountEventStore()
        val createAccount = CreateAccount(accountEventStore, { accountId })
        createAccount.execute()
        assertEquals(listOf(AccountCreated(accountId)), accountEventStore.eventsOf(accountId))
    }

}