package infra.command

import domain.makeADeposit
import infra.AccountEventStore
import kotlin.reflect.KClass

class MakeADepositHandler(private val accountEventStore: AccountEventStore): CommandHandler<MakeADeposit> {

    override fun listenTo() = MakeADeposit::class as KClass<Command>

    override fun handle(command: MakeADeposit) {
        val accountProjection = accountEventStore.retrieve(command.id)
        val decision = makeADeposit(command.amount, accountProjection)
        accountEventStore.save(decision)
    }
}