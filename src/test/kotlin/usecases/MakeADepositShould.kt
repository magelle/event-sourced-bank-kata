package usecases

import infra.command.CreateAccount
import infra.command.MakeADeposit
import domain.AccountCreated
import domain.DepositMade
import infra.AccountEventStore
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class MakeADepositShould {

    private val accountEventStore = AccountEventStore()

    private val accountId = 1

    @BeforeTest
    fun beforeTest() {
        val createAccount = CreateAccount(accountEventStore, { accountId })
        createAccount.execute()
    }

    @Test
    fun `create an account`() {
        MakeADeposit(accountId, 100, accountEventStore)
                .execute()
        assertEquals(
                listOf(AccountCreated(accountId), DepositMade(accountId, 100)),
                accountEventStore.eventsOf(accountId)
        )
    }


}