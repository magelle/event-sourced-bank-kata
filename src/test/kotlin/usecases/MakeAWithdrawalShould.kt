package usecases

import infra.command.CreateAccount
import infra.command.MakeAWithdrawal
import infra.command.MakeADeposit
import domain.AccountCreated
import domain.DepositMade
import domain.WithdrawalMade
import infra.AccountEventStore
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class MakeAWithdrawalShould {

    private val accountEventStore = AccountEventStore()

    private val accountId = 1

    @BeforeTest
    fun beforeTest() {
        CreateAccount(accountEventStore, { accountId }).execute()
        MakeADeposit(accountId, 300, accountEventStore).execute()
    }

    @Test
    fun `create an account`() {
        MakeAWithdrawal(accountId, 100, accountEventStore).execute()
        assertEquals(
                listOf(AccountCreated(accountId), DepositMade(accountId, 300), WithdrawalMade(accountId, 100)),
                accountEventStore.eventsOf(accountId)
        )
    }


}