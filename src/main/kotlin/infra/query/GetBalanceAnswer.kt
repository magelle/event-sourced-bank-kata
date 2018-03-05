package infra.query

import domain.AccountEvent

class GetBalanceAnswer(private val accountEventStore: (Int) -> List<AccountEvent>): QueryAnswer<GetBalance>
{
    override fun answerTo() = GetBalance::class

    override fun answer(query: GetBalance) = accountEventStore(query.accountId)
            .fold(BalanceProjection(), { acc, event -> acc.apply(event) })
            .let { Balance(it.balance) }
}