package infra.command

import domain.withdraw
import infra.AccountEventStore

class MakeAWithdrawal(private val id: Int, private val amount: Int, private val accountEventStore: AccountEventStore) {

    fun execute() {
        val accountProjection = accountEventStore.retrieve(id)
        val decision = withdraw(amount, accountProjection)
        accountEventStore.save(decision)
    }

}