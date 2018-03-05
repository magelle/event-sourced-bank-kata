package infra.query

class QueryDispatcher(answers: List<QueryAnswer<Query>>) {

    private val answerByQuery = answers.map { it.answerTo() to it }.toMap()

    fun dispatch(query: Query): Answer = answerByQuery[query::class]!!.answer(query)

}