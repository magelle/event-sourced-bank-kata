package usecases

import domain.AccountCreated
import domain.DepositMade
import domain.WithdrawalMade
import infra.query.GetBalance
import kotlin.test.Test
import kotlin.test.assertEquals

class GetBalanceShould {

    private val accountId = 1

    @Test
    fun `says zero when account is just create`() {
        val getBalance = GetBalance(accountId, { _ -> listOf(AccountCreated(accountId)) } )
        assertEquals(0, getBalance.get())
    }

    @Test
    fun `summ deposit and withdrawal`() {
        val getBalance = GetBalance(accountId, { _ -> listOf(
                AccountCreated(accountId),
                DepositMade(accountId, 100),
                WithdrawalMade(accountId, 50)) } )
        assertEquals(50, getBalance.get())
    }

}