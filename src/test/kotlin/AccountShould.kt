import kotlin.test.Test
import kotlin.test.assertEquals

class AccountShould {

    private fun idGenerator() = 1

    @Test
    fun `create a new account with balance 0`() {
        val account = newAccount(::idGenerator)
        assertEquals(account.events, listOf(AccountCreated(1)))
    }

    @Test
    fun `create a new account with a positive balance`() {
        val account = newAccount(::idGenerator, 100)
        assertEquals(account.events, listOf(
                AccountCreated(1),
                DepositMade(100)
                ))
    }

    @Test
    fun `made a deposit`() {
        val account = newAccount(::idGenerator).deposit(150)
        assertEquals(account.events, listOf(
                AccountCreated(1),
                DepositMade(150)
        ))
    }

    @Test
    fun `refuse a negative deposit`() {
        val account = newAccount(::idGenerator).deposit(-150)
        assertEquals(account.events, listOf(
                AccountCreated(1),
                DepositRefused(-150, "Negative deposit not allowed")
        ))
    }

    @Test
    fun `refuse a null deposit`() {
        val account = newAccount(::idGenerator).deposit(0)
        assertEquals(account.events, listOf(
                AccountCreated(1),
                DepositRefused(0, "Null deposit not allowed")
        ))
    }

    @Test
    fun `made a withdraw`() {
        val account = newAccount(::idGenerator, 200).withdraw(150)
        assertEquals(account.events, listOf(
                AccountCreated(1),
                DepositMade(200),
                WithdrawalMade(150)
        ))
    }

    @Test
    fun `refuse a negative withdrawal`() {
        val account = newAccount(::idGenerator, 200).withdraw(-150)
        assertEquals(account.events, listOf(
                AccountCreated(1),
                DepositMade(200),
                WithdrawalRefused(-150, "Negative withdrawal not allowed")
        ))
    }

    @Test
    fun `refuse a null withdrawal`() {
        val account = newAccount(::idGenerator, 200).withdraw(0)
        assertEquals(account.events, listOf(
                AccountCreated(1),
                DepositMade(200),
                WithdrawalRefused(0, "Null withdrawal not allowed")
        ))
    }

    @Test
    fun `refuse a withdrawal which put the balance in negative`() {
        val account = newAccount(::idGenerator, 200).withdraw(300)
        assertEquals(account.events, listOf(
                AccountCreated(1),
                DepositMade(200),
                WithdrawalRefused(300, "Negative balance not allowed")
        ))
    }

}