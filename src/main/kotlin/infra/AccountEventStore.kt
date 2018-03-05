package infra

import domain.AccountEvent
import domain.AccountProjection

class AccountEventStore(private var events: List<AccountEvent> = mutableListOf()) {

    fun save(event: AccountEvent) {
        events += event
    }

    fun eventsOf(id: Int) = events.filter { id == it.id }.toList()

    fun retrieve(id: Int) = eventsOf(id)
            .fold( AccountProjection(), { proj, event -> event.applyOn(proj) } )
}
