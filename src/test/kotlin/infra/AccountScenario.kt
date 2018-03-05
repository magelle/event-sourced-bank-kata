package infra

import infra.command.CreateAccount
import infra.command.MakeADeposit
import infra.command.MakeAWithdrawal
import infra.query.GetBalance
import kotlin.test.Test
import kotlin.test.assertEquals

class AccountScenario {

    private val accountId: Int = 2
    private val idGenerator = { accountId }
    private val accountEventStore = AccountEventStore()

    @Test
    fun `return the right balance`() {
        val getBalance = GetBalance(accountId, { id -> accountEventStore.eventsOf(id) })

        CreateAccount(idGenerator, accountEventStore).execute()
        assertEquals(0, getBalance.get())

        MakeADeposit(accountId, 250, accountEventStore).execute()
        assertEquals(250, getBalance.get())

        MakeADeposit(accountId, 100, accountEventStore).execute()
        assertEquals(350, getBalance.get())

        MakeAWithdrawal(accountId, 250, accountEventStore).execute()
        assertEquals(100, getBalance.get())
    }

}