package infra.query

import domain.AccountEvent
import domain.DepositMade
import domain.WithdrawalMade

class GetBalance(val accountId: Int, private val accountEventStore: (Int) -> List<AccountEvent>) {

    fun get() = accountEventStore(accountId).fold(0, { acc, event ->
        when (event) {
            is DepositMade -> acc + event.amount
            is WithdrawalMade -> acc - event.amount
            else -> acc
        }
    })
}