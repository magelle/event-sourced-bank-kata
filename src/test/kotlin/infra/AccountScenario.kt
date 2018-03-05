package infra

import infra.command.*
import infra.query.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AccountScenario {

    private val accountId: Int = 2
    private val idGenerator = { accountId }
    private val accountEventStore = AccountEventStore()
    private val commandDispatcher: CommandDispatcher
    private val queryDispatcher: QueryDispatcher
    init {
        val createAccountHandler = CreateAccountHandler(idGenerator, accountEventStore)
        val makeADepositHandler = MakeADepositHandler(accountEventStore)
        val makeAWithdrawalHandler = MakeAWithdrawalHandler(accountEventStore)
        commandDispatcher = CommandDispatcher(listOf(createAccountHandler, makeADepositHandler, makeAWithdrawalHandler) as List<CommandHandler<in Command>>)

        val getBalanceAnswer = GetBalanceAnswer({ accountEventStore.eventsOf(it) })
        queryDispatcher = QueryDispatcher(listOf(getBalanceAnswer) as List<QueryAnswer<Query>>)
    }

    @Test
    fun `return the right balance`() {
        val getBalance = { (queryDispatcher.dispatch(GetBalance(accountId)) as Balance).balance }

        commandDispatcher.dispatch(CreateAccount())
        assertEquals(0, getBalance())

        commandDispatcher.dispatch(MakeADeposit(accountId, 250))
        assertEquals(250, getBalance())

        commandDispatcher.dispatch(MakeADeposit(accountId, 100))
        assertEquals(350, getBalance())

        commandDispatcher.dispatch(MakeAWithdrawal(accountId, 250))
        assertEquals(100, getBalance())
    }

}