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
	return typedCriteria.fold(Criteria()) { chain, head -> head.operation(chain.and(head.name))}
}

infix fun <T> KProperty<T>.isEqualTo(value: T) = buildCriteria { isEqualTo(value) }
infix fun <T> KProperty<T>.ne(value: T) = buildCriteria { ne(value) }
infix fun <T> KProperty<T>.lt(value: T) = buildCriteria { lt(value) }
infix fun <T> KProperty<T>.lte(value: T) = buildCriteria { lte(value) }
infix fun <T> KProperty<T>.gt(value: T) = buildCriteria { gt(value) }
infix fun <T> KProperty<T>.gte(value: T) = buildCriteria { gte(value) }
infix fun <T> KProperty<T>.inValues(value: Collection<T>) = buildCriteria { `in`(value) }
fun <T> KProperty<T>.inValues(vararg o: Any) = buildCriteria { `in`(*o) }
infix fun <T> KProperty<T>.nin(value: Collection<T>) = buildCriteria { nin(value) }
fun <T> KProperty<T>.nin(vararg o: Any) = buildCriteria { nin(*o) }
fun KProperty<Number>.mod(value: Number, remainder: Number) = buildCriteria { mod(value, remainder) }
infix fun <T : Collection<*>> KProperty<T>.all(value: T) = buildCriteria { all(value) }
fun <T> KProperty<T>.all(vararg o: Any) = buildCriteria { all(*o) }
infix fun KProperty<Collection<*>>.size(s: Int) = buildCriteria { size(s) }

private fun <T> KProperty<T>.buildCriteria(operation: Criteria.() -> Criteria) = TypedCriteria(this, operation)
