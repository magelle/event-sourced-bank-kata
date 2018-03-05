package infra

import infra.command.*
import infra.query.GetBalance
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals

class AccountScenario {

    private val accountId: Int = 2
    private val idGenerator = { accountId }
    private val accountEventStore = AccountEventStore()
    private val commandDispatcher: CommandDispatcher
    init {
        val createAccountHandler = CreateAccountHandler(idGenerator, accountEventStore)
        val makeADepositHandler = MakeADepositHandler(accountEventStore)
        val makeAWithdrawalHandler = MakeAWithdrawalHandler(accountEventStore)
        commandDispatcher = CommandDispatcher(mapOf(
                createAccountHandler.listenTo() to createAccountHandler,
                makeADepositHandler.listenTo() to makeADepositHandler,
                makeAWithdrawalHandler.listenTo() to makeAWithdrawalHandler
        ) as Map<KClass<out Command>, CommandHandler<in Command>>)
    }

    @Test
    fun `return the right balance`() {
        val getBalance = GetBalance(accountId, { id -> accountEventStore.eventsOf(id) })

        commandDispatcher.dispatch(CreateAccount())
        assertEquals(0, getBalance.get())

        commandDispatcher.dispatch(MakeADeposit(accountId, 250))
        assertEquals(250, getBalance.get())

        commandDispatcher.dispatch(MakeADeposit(accountId, 100))
        assertEquals(350, getBalance.get())

        commandDispatcher.dispatch(MakeAWithdrawal(accountId, 250))
        assertEquals(100, getBalance.get())
    }

}