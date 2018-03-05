package domain

import kotlin.test.Test
import kotlin.test.assertEquals

class AccountShould {

    private val accountId = 1

    private fun idGenerator() = accountId

    @Test
    fun `create a new account with balance 0`() {
        assertEquals(createAnAccount(::idGenerator), AccountCreated(accountId))
    }

    @Test
    fun `made a deposit`() {
        val event = makeADeposit(150, accountProjection(accountId, 0))
        assertEquals(event, DepositMade(accountId, 150))
    }

    @Test
    fun `refuse a negative deposit`() {
        val event = makeADeposit(-150, accountProjection(accountId, 0))
        assertEquals(event, DepositRefused(accountId, -150, "Negative deposit not allowed"))
    }

    @Test
    fun `refuse a null deposit`() {
        val event = makeADeposit(0, accountProjection(accountId, 0))
        assertEquals(event, DepositRefused(accountId, 0, "Null deposit not allowed"))
    }

    @Test
    fun `made a withdraw`() {
        val event = withdraw(150, accountProjection(accountId, 200))
        assertEquals(event, WithdrawalMade(accountId, 150))
    }

    @Test
    fun `refuse a negative withdrawal`() {
        val event = withdraw(-150, accountProjection(accountId, 200))
        assertEquals(event, WithdrawalRefused(accountId, -150, "Negative withdrawal not allowed"))
    }

    @Test
    fun `refuse a null withdrawal`() {
        val event = withdraw(0, accountProjection(accountId, 200))
        assertEquals(event, WithdrawalRefused(accountId, 0, "Null withdrawal not allowed"))
    }

    @Test
    fun `refuse a withdrawal which put the balance in negative`() {
        val event = withdraw(300, accountProjection(accountId, 200))
        assertEquals(event, WithdrawalRefused(accountId, 300, "Negative balance not allowed"))
    }

}

fun accountProjection(id: Int, balance: Int) = TestableAccountProjection(id, balance)
data class TestableAccountProjection(override val id: Int, override val balance: Int) : AccountProjection(id, balance)