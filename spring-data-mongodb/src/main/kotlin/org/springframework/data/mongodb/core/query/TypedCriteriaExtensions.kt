/*
 * Copyright 2010-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mongodb.core.query

import org.bson.Document
import kotlin.reflect.KProperty

/**
 * @author Tjeu Kayim
 */
class TypedCriteria(
	property: KProperty<Any?>,
	val operation: Criteria.() -> Criteria
) : CriteriaDefinition {
	val name = property.name
	val criteria by lazy { Criteria(name).operation() }

	override fun getCriteriaObject(): Document = criteria.criteriaObject

	override fun getKey(): String? = criteria.key
}

fun typedCriteria(vararg typedCriteria: TypedCriteria): CriteriaDefinition {
	return chainCriteria(Criteria(), typedCriteria.toList())
}

private fun chainCriteria(criteria: Criteria, tail: Collection<TypedCriteria>): Criteria {
	if (tail.isEmpty()) {
		return criteria
	}
	val head = tail.first()
	head.operation(criteria.and(head.name))
	return chainCriteria(criteria, tail.drop(1))
}

infix fun <T> KProperty<T>.isEqualTo(value: T) = filter { isEqualTo(value) }
infix fun <T> KProperty<T>.ne(value: T) = filter { ne(value) }
infix fun <T> KProperty<T>.lt(value: T) = filter { lt(value) }
infix fun <T> KProperty<T>.lte(value: T) = filter { lte(value) }
infix fun <T> KProperty<T>.gt(value: T) = filter { gt(value) }
infix fun <T> KProperty<T>.gte(value: T) = filter { gte(value) }
infix fun <T> KProperty<T>.inValues(value: Collection<T>) = filter { `in`(value) }
fun <T> KProperty<T>.inValues(vararg o: Any) = filter { `in`(*o) }
infix fun <T> KProperty<T>.nin(value: Collection<T>) = filter { nin(value) }
fun <T> KProperty<T>.nin(vararg o: Any) = filter { nin(*o) }
fun KProperty<Number>.mod(value: Number, remainder: Number) = filter { mod(value, remainder) }
infix fun <T : Collection<*>> KProperty<T>.all(value: T) = filter { all(value) }
fun <T> KProperty<T>.all(vararg o: Any) = filter { all(*o) }
fun KProperty<Collection<*>>.size(s: Int) = filter { size(s) }

private fun <T> KProperty<T>.filter(operation: Criteria.() -> Criteria) = TypedCriteria(this, operation)
