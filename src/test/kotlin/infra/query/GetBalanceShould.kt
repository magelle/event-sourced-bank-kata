package infra.query

import domain.AccountCreated
import domain.DepositMade
import domain.WithdrawalMade
import kotlin.test.Test
import kotlin.test.assertEquals

class GetBalanceShould {

    private val accountId = 1

    @Test
    fun `says zero when account is just create`() {
        val events = { _: Int -> listOf(AccountCreated(accountId)) }
        val getBalance = GetBalance(accountId)
        val getBalanceAnswer = GetBalanceAnswer(events)
        val balance = getBalanceAnswer.answer(getBalance)
        assertEquals(0, balance.balance)
    }

    @Test
    fun `sum deposit and withdrawal`() {
        val events = { _: Int -> listOf(
                AccountCreated(accountId),
                DepositMade(accountId, 100),
                WithdrawalMade(accountId, 50)) }
        val getBalance = GetBalance(accountId)
        val getBalanceAnswer = GetBalanceAnswer(events)
        val balance = getBalanceAnswer.answer(getBalance)
        assertEquals(50, balance.balance)
    }

}