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
import kotlin.reflect.KProperty1

/**
 * @author Tjeu Kayim
 */
class TypedCriteria<DOC> : CriteriaDefinition<DOC> {
	override fun getCriteriaObject(): Document = chain.criteriaObject

	override fun getKey(): String? = chain.key

	private var chain = Criteria()

	infix fun <T : Any> KProperty1<DOC, T>.gt(value: T) {
		chain = chain.and(name).gt(value)
	}

	infix fun <T : Any> KProperty1<DOC, T>.isEqualTo(value: T) {
		chain = chain.and(name).isEqualTo(value)
	}
}

fun <T> typedCriteria(block: TypedCriteria<T>.() -> Unit): CriteriaDefinition<T> {
	val typedCriteria = TypedCriteria<T>()
	typedCriteria.block()
	return typedCriteria
}
