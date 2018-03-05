package infra.command

import domain.AccountCreated
import domain.DepositMade
import infra.AccountEventStore
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class MakeADepositHandlerShould {

    private val accountEventStore = AccountEventStore()
    private val accountId = 1

    private lateinit var createAccountHandler: CreateAccountHandler

    @BeforeTest
    fun beforeTest() {
        createAccountHandler = CreateAccountHandler({ accountId }, accountEventStore)
        createAccountHandler.handle(CreateAccount())
    }

    @Test
    fun `create an account`() {
        val makeAWithdrawalHandler = MakeADepositHandler(accountEventStore)
        makeAWithdrawalHandler.handle(MakeADeposit(accountId, 100))
        assertEquals(
                listOf(AccountCreated(accountId), DepositMade(accountId, 100)),
                accountEventStore.eventsOf(accountId)
        )
    }


}