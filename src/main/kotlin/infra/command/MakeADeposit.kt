package infra.command

import domain.makeADeposit
import infra.AccountEventStore

class MakeADeposit(private val id: Int, private val amount: Int, private val accountEventStore: AccountEventStore) {

    fun execute() {
        val accountProjection = accountEventStore.retrieve(id)
        val decision = makeADeposit(amount, accountProjection)
        accountEventStore.save(decision)
    }

}