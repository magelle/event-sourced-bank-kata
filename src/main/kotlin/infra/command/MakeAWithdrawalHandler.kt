package infra.command

import domain.withdraw
import infra.AccountEventStore

class MakeAWithdrawalHandler(val accountEventStore: AccountEventStore): CommandHandler<MakeAWithdrawal> {

    override fun listenTo() = MakeAWithdrawal::class

    override fun handle(command: MakeAWithdrawal) {
        val accountProjection = accountEventStore.retrieve(command.id)
        val decision = withdraw(command.amount, accountProjection)
        accountEventStore.save(decision)
    }
}