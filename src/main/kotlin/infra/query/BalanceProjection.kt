package infra.query

import domain.AccountEvent
import domain.DepositMade
import domain.WithdrawalMade

class BalanceProjection(val balance: Int = 0) {

    fun apply(event: AccountEvent) = BalanceProjection(
        when (event) {
            is DepositMade -> balance + event.amount
            is WithdrawalMade -> balance - event.amount
            else -> balance
        })

}