package infra.query

import kotlin.reflect.KClass

interface QueryAnswer<in T> {

    fun answerTo(): KClass<*>
    fun answer(query: T): Answer

}