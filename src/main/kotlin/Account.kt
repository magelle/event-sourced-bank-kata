class Account(val events: List<AccountEvent>) {

    private val projection: AccountProjection

    init {
        projection = events.fold(AccountProjection(), { projection, event -> event.applyOn(projection) } )
    }

    fun deposit(amount: Int) = Account(events + decisionOnDeposit(amount))
    fun withdraw(amount: Int) = Account(events + decisionOnWithdrawal(amount, projection))
}

fun newAccount(id: () -> Int) = Account(mutableListOf(AccountCreated(id())))
fun newAccount(id: () -> Int, amount: Int) = Account(mutableListOf(AccountCreated(id()), DepositMade(amount)))
private fun decisionOnDeposit(amount: Int) = when {
        amount < 0 -> DepositRefused(amount, "Negative deposit not allowed")
        amount == 0 -> DepositRefused(amount, "Null deposit not allowed")
        else -> DepositMade(amount)}

private fun decisionOnWithdrawal(amount: Int, projection: AccountProjection): AccountEvent {
    return when {
        amount < 0 -> WithdrawalRefused(amount, "Negative withdrawal not allowed")
        amount == 0 -> WithdrawalRefused(amount, "Null withdrawal not allowed")
        projection.balance - amount < 0 -> WithdrawalRefused(amount, "Negative balance not allowed")
        else -> WithdrawalMade(amount)
    }
}

interface AccountEvent {
    fun applyOn(proj: AccountProjection): AccountProjection
}

data class AccountCreated(val id: Int): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

data class DepositMade(val amount: Int): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

data class DepositRefused(val amount: Int, val cause: String): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

data class WithdrawalMade(val amount: Int): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

data class WithdrawalRefused(val amount: Int, val cause: String): AccountEvent {
    override fun applyOn(proj: AccountProjection) = proj.apply(this)
}

data class AccountProjection(val balance: Int = 0) {
    fun apply(event: AccountCreated) = AccountProjection(0)
    fun apply(event: DepositMade) = AccountProjection(balance + event.amount)
    fun apply(event: DepositRefused) = this
    fun apply(event: WithdrawalMade) = AccountProjection(balance - event.amount)
    fun apply(event: WithdrawalRefused) = this
}