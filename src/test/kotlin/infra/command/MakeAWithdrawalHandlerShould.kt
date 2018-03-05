package infra.command

import domain.AccountCreated
import domain.DepositMade
import domain.WithdrawalMade
import infra.AccountEventStore
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class MakeAWithdrawalHandlerShould {

    private val accountEventStore = AccountEventStore()
    private val accountId = 1

    private lateinit var createAccountHandler: CreateAccountHandler

    @BeforeTest
    fun beforeTest() {
        createAccountHandler = CreateAccountHandler({ accountId }, accountEventStore)
        createAccountHandler.handle(CreateAccount())
        val makeAWithdrawalHandler = MakeADepositHandler(accountEventStore)
        makeAWithdrawalHandler.handle(MakeADeposit(accountId, 300))
    }

    @Test
    fun `create an account`() {
        val makeAWithdrawalHandler = MakeAWithdrawalHandler(accountEventStore)
        makeAWithdrawalHandler.handle(MakeAWithdrawal(accountId, 100))
        assertEquals(
                listOf(AccountCreated(accountId), DepositMade(accountId, 300), WithdrawalMade(accountId, 100)),
                accountEventStore.eventsOf(accountId)
        )
    }


}