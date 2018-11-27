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

import org.bson.BsonRegularExpression
import org.bson.types.ObjectId
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Point
import org.springframework.data.geo.Shape
import org.springframework.data.mongodb.core.geo.GeoJson
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.schema.JsonSchemaObject.Type
import java.util.regex.Pattern
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

typealias TypedOperations = TypedCriteriaBuilder.() -> Unit

/**
 * @author Tjeu Kayim
 */
class TypedOperation(
	property: KProperty<Any?>,
	val operation: Criteria.() -> Criteria
) {
	val name = when(property) {
		is NestedProperty<*, *> -> "${property.parent.name}.${property.child.name}"
		else -> property.name
	}
	val criteria by lazy { Criteria(name).operation() }
}

/**
 * Typed criteria builder.
 *
 * @sample typedCriteriaSample
 *
 * @see typedQuery
 * @author Tjeu Kayim
 */
fun typedCriteria(operations: TypedOperations): Criteria {
	val builder = TypedCriteriaBuilder().apply(operations)
	return builder.build()
}

/**
 * Used by [typedCriteria] to build typed criteria.
 *
 * @see typedCriteria
 * @author Tjeu Kayim
 */
class TypedCriteriaBuilder {
	private var criteria = Criteria()
	private val operations = mutableListOf<TypedOperation>()

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
	infix fun KProperty<*>.all(value: Collection<*>) = buildCriteria { all(value) }
	fun KProperty<*>.all(vararg o: Any) = buildCriteria { all(*o) }
	infix fun KProperty<*>.size(s: Int) = buildCriteria { size(s) }
	infix fun KProperty<*>.exists(b: Boolean) = buildCriteria { exists(b) }
	infix fun KProperty<*>.type(t: Int) = buildCriteria { type(t) }
	infix fun KProperty<*>.type(t: Array<Type>) = buildCriteria { type(*t) }
	fun KProperty<*>.not() = buildCriteria { not() }
	infix fun KProperty<*>.regex(re: String) = buildCriteria { regex(re, null) }
	fun KProperty<*>.regex(re: String, options: String?) = buildCriteria { regex(re, options) }
	infix fun KProperty<*>.regex(re: Regex) = buildCriteria { regex(re.toPattern()) }
	infix fun KProperty<*>.regex(re: Pattern) = buildCriteria { regex(re) }
	infix fun KProperty<*>.regex(re: BsonRegularExpression) = buildCriteria { regex(re) }
	infix fun KProperty<*>.withinSphere(circle: Circle) = buildCriteria { withinSphere(circle) }
	infix fun KProperty<*>.within(shape: Shape) = buildCriteria { within(shape) }
	infix fun KProperty<*>.near(point: Point) = buildCriteria { near(point) }
	infix fun KProperty<*>.nearSphere(point: Point) = buildCriteria { nearSphere(point) }
	infix fun KProperty<*>.intersects(geoJson: GeoJson<*>) = buildCriteria { intersects(geoJson) }
	infix fun KProperty<*>.maxDistance(d: Double) = buildCriteria { maxDistance(d) }
	infix fun KProperty<*>.minDistance(d: Double) = buildCriteria { minDistance(d) }
	infix fun KProperty<*>.elemMatch(c: Criteria) = buildCriteria { elemMatch(c) }
	infix fun KProperty<*>.elemMatch(c: TypedOperations) = buildCriteria { elemMatch(typedCriteria(c)) }

	/**
	 * Creates an 'or' criteria using the $or operator.
	 */
	fun or(other: TypedCriteriaBuilder.() -> Unit) {
		build()
		criteria.orOperator(*TypedCriteriaBuilder().apply(other).operations.map { it.criteria }.toTypedArray())
	}

	infix fun <T, U> KProperty<T>.nest(other: KProperty1<T, U>) =
		NestedProperty(this, other)

	private fun <T> KProperty<T>.buildCriteria(operation: Criteria.() -> Criteria): TypedOperation {
		val typedCriteria = TypedOperation(this, operation)
		operations.add(typedCriteria)
		return typedCriteria
	}

	/**
	 * Apply all operations to criteria.
	 */
	internal fun build(): Criteria {
		criteria = operations.fold(criteria) { chain, head -> head.operation(chain.and(head.name)) }
		operations.clear()
		return criteria
	}
}

class NestedProperty<T, U>(
	val parent: KProperty<T>,
	val child: KProperty1<T, U>
) : KProperty<U> by child

private fun typedCriteriaSample() {
	@Document("books")
	data class Book(
		val id: ObjectId, val name: String,
		val price: Int, val categories: List<String>
	)
	// Build typed criteria
	typedCriteria {
		Book::name isEqualTo "Moby-Dick"
		Book::price lt 950
	}
	// $or operator
	typedCriteria {
		Book::name isEqualTo "Moby-Dick"
		or {
			Book::price lt 1200
			Book::price gt 240
		}
	}
}
