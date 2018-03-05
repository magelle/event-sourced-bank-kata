package domain

fun createAnAccount(id: () -> Int) = AccountCreated(id())

fun makeADeposit(amount: Int, projection: AccountProjection) = when {
        amount < 0 -> DepositRefused(projection.id, amount, "Negative deposit not allowed")
        amount == 0 -> DepositRefused(projection.id, amount, "Null deposit not allowed")
        else -> DepositMade(projection.id, amount)
}

fun withdraw(amount: Int, projection: AccountProjection): AccountEvent {
    return when {
        amount < 0 -> WithdrawalRefused(projection.id, amount, "Negative withdrawal not allowed")
        amount == 0 -> WithdrawalRefused(projection.id, amount, "Null withdrawal not allowed")
        projection.balance - amount < 0 -> WithdrawalRefused(projection.id, amount, "Negative balance not allowed")
        else -> WithdrawalMade(projection.id, amount)
    }
}

interface AccountEvent {
    val id: Int
    fun applyOn(proj: AccountProjection): AccountProjection
}

data class AccountCreated(override val id: Int): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

data class DepositMade(override val id: Int, val amount: Int): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

data class DepositRefused(override val id: Int, val amount: Int, val cause: String): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

data class WithdrawalMade(override val id: Int, val amount: Int): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

data class WithdrawalRefused(override val id: Int, val amount: Int, val cause: String): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

private fun createAccountProjection(events: List<AccountEvent>) =
        events.fold(AccountProjection(0), { projection, event -> event.applyOn(projection) })

open class AccountProjection(open val id: Int = 0, open val balance: Int = 0) {
    fun apply(event: AccountCreated) = AccountProjection(event.id, 0)
    fun apply(event: DepositMade) = AccountProjection(id, balance + event.amount)
    fun apply(event: DepositRefused) = this
    fun apply(event: WithdrawalMade) = AccountProjection(id, balance - event.amount)
    fun apply(event: WithdrawalRefused) = this
}