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
import org.springframework.data.domain.Example
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Point
import org.springframework.data.geo.Shape
import org.springframework.data.mongodb.core.geo.GeoJson
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.schema.JsonSchemaObject.Type
import org.springframework.data.mongodb.core.schema.MongoJsonSchema
import java.util.regex.Pattern
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

typealias TypedOperations = TypedCriteriaBuilder.() -> Unit

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
	return builder.chainCriteria()
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

	infix fun <T> KProperty<T>.isEqualTo(value: T) = addOperation { isEqualTo(value) }
	infix fun <T> KProperty<T>.ne(value: T) = addOperation { ne(value) }
	infix fun <T> KProperty<T>.lt(value: T) = addOperation { lt(value) }
	infix fun <T> KProperty<T>.lte(value: T) = addOperation { lte(value) }
	infix fun <T> KProperty<T>.gt(value: T) = addOperation { gt(value) }
	infix fun <T> KProperty<T>.gte(value: T) = addOperation { gte(value) }
	infix fun <T> KProperty<T>.inValues(value: Collection<T>) = addOperation { `in`(value) }
	fun <T> KProperty<T>.inValues(vararg o: Any) = addOperation { `in`(*o) }
	infix fun <T> KProperty<T>.nin(value: Collection<T>) = addOperation { nin(value) }
	fun <T> KProperty<T>.nin(vararg o: Any) = addOperation { nin(*o) }
	fun KProperty<Number>.mod(value: Number, remainder: Number) = addOperation { mod(value, remainder) }
	infix fun KProperty<*>.all(value: Collection<*>) = addOperation { all(value) }
	fun KProperty<*>.all(vararg o: Any) = addOperation { all(*o) }
	infix fun KProperty<*>.size(s: Int) = addOperation { size(s) }
	infix fun KProperty<*>.exists(b: Boolean) = addOperation { exists(b) }
	infix fun KProperty<*>.type(t: Int) = addOperation { type(t) }
	infix fun KProperty<*>.type(t: Array<Type>) = addOperation { type(*t) }
	fun KProperty<*>.not() = addOperation { not() }
	infix fun KProperty<*>.regex(re: String) = addOperation { regex(re, null) }
	fun KProperty<*>.regex(re: String, options: String?) = addOperation { regex(re, options) }
	infix fun KProperty<*>.regex(re: Regex) = addOperation { regex(re.toPattern()) }
	infix fun KProperty<*>.regex(re: Pattern) = addOperation { regex(re) }
	infix fun KProperty<*>.regex(re: BsonRegularExpression) = addOperation { regex(re) }
	infix fun KProperty<*>.withinSphere(circle: Circle) = addOperation { withinSphere(circle) }
	infix fun KProperty<*>.within(shape: Shape) = addOperation { within(shape) }
	infix fun KProperty<*>.near(point: Point) = addOperation { near(point) }
	infix fun KProperty<*>.nearSphere(point: Point) = addOperation { nearSphere(point) }
	infix fun KProperty<*>.intersects(geoJson: GeoJson<*>) = addOperation { intersects(geoJson) }
	infix fun KProperty<*>.maxDistance(d: Double) = addOperation { maxDistance(d) }
	infix fun KProperty<*>.minDistance(d: Double) = addOperation { minDistance(d) }
	infix fun KProperty<*>.elemMatch(c: Criteria) = addOperation { elemMatch(c) }
	infix fun KProperty<*>.elemMatch(c: TypedOperations) = addOperation { elemMatch(typedCriteria(c)) }
	infix fun KProperty<*>.alike(sample: Example<*>) = addOperation { alike(sample) }
	infix fun KProperty<*>.andDocumentStructureMatches(schema: MongoJsonSchema) =
		addOperation { andDocumentStructureMatches(schema) }

	infix fun KProperty<*>.bits(bitwiseCriteria: Criteria.BitwiseCriteriaOperators.() -> Criteria) =
		addOperation { bits().let(bitwiseCriteria) }


	private fun <T> KProperty<T>.addOperation(operation: Criteria.() -> Criteria): TypedOperation {
		val typedOperation = TypedOperation(this, operation)
		operations.add(typedOperation)
		return typedOperation
	}

	fun or(builder: TypedOperations) = addOperatorWithCriteria(builder, Criteria::orOperator)
	fun nor(builder: TypedOperations) = addOperatorWithCriteria(builder, Criteria::norOperator)
	fun and(builder: TypedOperations) = addOperatorWithCriteria(builder, Criteria::andOperator)

	private fun addOperatorWithCriteria(builder: TypedOperations, operation: Criteria.(Array<Criteria>) -> Criteria) {
		val otherCriteria = TypedCriteriaBuilder().apply(builder).listCriteria()
		chainCriteria()
		criteria.operation(otherCriteria.toTypedArray())
	}

	/**
	 * Build nested properties.
	 */
	operator fun <T, U> KProperty<T>.div(other: KProperty1<T, U>) =
		NestedProperty(this, other)

	/**
	 * Apply all operations to one criteria.
	 */
	internal fun chainCriteria(): Criteria {
		criteria = operations.fold(criteria) { chain, head -> head.operation(chain.and(head.name)) }
		operations.clear()
		return criteria
	}

	/**
	 * Map each operation to a criteria.
	 */
	private fun listCriteria(): List<Criteria> {
		return operations.map { it.criteria }
	}
}

/**
 * @author Tjeu Kayim
 */
class TypedOperation(
	property: KProperty<Any?>,
	val operation: Criteria.() -> Criteria
) {
	val name = nestedFieldName(property)
	val criteria by lazy { Criteria(name).operation() }

	private fun nestedFieldName(property: KProperty<*>): String {
		return when (property) {
			is NestedProperty<*, *> ->
				"${nestedFieldName(property.parent)}.${property.child.name}"
			else -> property.name
		}
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
