package infra.query

import domain.AccountEvent
import domain.DepositMade
import domain.WithdrawalMade

class GetBalanceAnswer(private val accountEventStore: (Int) -> List<AccountEvent>): QueryAnswer<GetBalance>
{
    override fun answerTo() = GetBalance::class

    override fun answer(query: GetBalance) = accountEventStore(query.accountId)
            .fold(0, { acc, event ->
                when (event) {
                    is DepositMade -> acc + event.amount
                    is WithdrawalMade -> acc - event.amount
                    else -> acc
                }
            })
            .let { Balance(it) }
}