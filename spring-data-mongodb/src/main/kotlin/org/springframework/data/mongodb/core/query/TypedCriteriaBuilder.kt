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
import org.springframework.data.domain.Example
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Point
import org.springframework.data.geo.Shape
import org.springframework.data.mongodb.core.geo.GeoJson
import org.springframework.data.mongodb.core.schema.JsonSchemaObject
import org.springframework.data.mongodb.core.schema.MongoJsonSchema
import java.util.regex.Pattern
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

typealias TypedCriteria = TypedCriteriaBuilder.() -> Unit

/**
 * Builds typed criteria.
 * Used as receiver for [typedCriteria].
 *
 * @see typedCriteria
 * @author Tjeu Kayim
 */
class TypedCriteriaBuilder {
	private var criteria = Criteria()
	private val operations = mutableListOf<Operation>()

	/**
	 * Creates a criterion using equality.
	 * @see Criteria.isEqualTo
	 */
	infix fun <T> KProperty<T>.isEqualTo(value: T) = addOperation { isEqualTo(value) }

	/**
	 * Creates a criterion using the $ne operator.
	 *
	 * See [MongoDB Query operator: $ne](https://docs.mongodb.com/manual/reference/operator/query/ne/)
	 * @see Criteria.ne
	 */
	infix fun <T> KProperty<T>.ne(value: T) = addOperation { ne(value) }

	/**
	 * Creates a criterion using the $lt operator.
	 *
	 * See [MongoDB Query operator: $lt](https://docs.mongodb.com/manual/reference/operator/query/lt/)
	 * @see Criteria.lt
	 */
	infix fun <T> KProperty<T>.lt(value: T) = addOperation { lt(value) }

	/**
	 * Creates a criterion using the $lte operator.
	 *
	 * See [MongoDB Query operator: $lte](https://docs.mongodb.com/manual/reference/operator/query/lte/)
	 * @see Criteria.lte
	 */
	infix fun <T> KProperty<T>.lte(value: T) = addOperation { lte(value) }

	/**
	 * Creates a criterion using the $gt operator.
	 *
	 * See [MongoDB Query operator: $gt](https://docs.mongodb.com/manual/reference/operator/query/gt/)
	 * @see Criteria.gt
	 */
	infix fun <T> KProperty<T>.gt(value: T) = addOperation { gt(value) }

	/**
	 * Creates a criterion using the $gte operator.
	 *
	 * See [MongoDB Query operator: $gte](https://docs.mongodb.com/manual/reference/operator/query/gte/)
	 * @see Criteria.gte
	 */
	infix fun <T> KProperty<T>.gte(value: T) = addOperation { gte(value) }

	/**
	 * Creates a criterion using the $in operator.
	 *
	 * See [MongoDB Query operator: $in](https://docs.mongodb.com/manual/reference/operator/query/in/)
	 * @see Criteria.inValues
	 */
	fun <T> KProperty<T>.inValues(vararg o: Any) = addOperation { `in`(*o) }

	/**
	 * Creates a criterion using the $in operator.
	 *
	 * See [MongoDB Query operator: $in](https://docs.mongodb.com/manual/reference/operator/query/in/)
	 * @see Criteria.inValues
	 */
	infix fun <T> KProperty<T>.inValues(value: Collection<T>) = addOperation { `in`(value) }

	/**
	 * Creates a criterion using the $nin operator.
	 *
	 * See [MongoDB Query operator: $nin](https://docs.mongodb.com/manual/reference/operator/query/nin/)
	 * @see Criteria.nin
	 */
	fun <T> KProperty<T>.nin(vararg o: Any) = addOperation { nin(*o) }

	/**
	 * Creates a criterion using the $nin operator.
	 *
	 * See [MongoDB Query operator: $nin](https://docs.mongodb.com/manual/reference/operator/query/nin/)
	 * @see Criteria.nin
	 */
	infix fun <T> KProperty<T>.nin(value: Collection<T>) = addOperation { nin(value) }

	/**
	 * Creates a criterion using the $mod operator.
	 *
	 * See [MongoDB Query operator: $mod](https://docs.mongodb.com/manual/reference/operator/query/mod/)
	 * @see Criteria.mod
	 */
	fun KProperty<Number>.mod(value: Number, remainder: Number) = addOperation { mod(value, remainder) }

	/**
	 * Creates a criterion using the $all operator.
	 *
	 * See [MongoDB Query operator: $all](https://docs.mongodb.com/manual/reference/operator/query/all/)
	 * @see Criteria.all
	 */
	fun KProperty<*>.all(vararg o: Any) = addOperation { all(*o) }

	/**
	 * Creates a criterion using the $all operator.
	 *
	 * See [MongoDB Query operator: $all](https://docs.mongodb.com/manual/reference/operator/query/all/)
	 * @see Criteria.all
	 */
	infix fun KProperty<*>.all(value: Collection<*>) = addOperation { all(value) }

	/**
	 * Creates a criterion using the $size operator.
	 *
	 * See [MongoDB Query operator: $size](https://docs.mongodb.com/manual/reference/operator/query/size/)
	 * @see Criteria.size
	 */
	infix fun KProperty<*>.size(s: Int) = addOperation { size(s) }

	/**
	 * Creates a criterion using the $exists operator.
	 *
	 * See [MongoDB Query operator: $exists](https://docs.mongodb.com/manual/reference/operator/query/exists/)
	 * @see Criteria.exists
	 */
	infix fun KProperty<*>.exists(b: Boolean) = addOperation { exists(b) }

	/**
	 * Creates a criterion using the $type operator.
	 *
	 * See [MongoDB Query operator: $type](https://docs.mongodb.com/manual/reference/operator/query/type/)
	 * @see Criteria.type
	 */
	infix fun KProperty<*>.type(t: Int) = addOperation { type(t) }

	/**
	 * Creates a criterion using the $type operator.
	 *
	 * See [MongoDB Query operator: $type](https://docs.mongodb.com/manual/reference/operator/query/type/)
	 * @see Criteria.type
	 */
	infix fun KProperty<*>.type(t: Array<JsonSchemaObject.Type>) = addOperation { type(*t) }

	/**
	 * Creates a criterion using the $not meta operator which affects the clause directly following
	 *
	 * See [MongoDB Query operator: $not](https://docs.mongodb.com/manual/reference/operator/query/not/)
	 * @see Criteria.not
	 */
	fun KProperty<*>.not() = addOperation { not() }

	/**
	 * Creates a criterion using a $regex operator.
	 *
	 * See [MongoDB Query operator: $regex](https://docs.mongodb.com/manual/reference/operator/query/regex/)
	 * @see Criteria.regex
	 */
	infix fun KProperty<*>.regex(re: String) = addOperation { regex(re, null) }

	/**
	 * Creates a criterion using a $regex and $options operator.
	 *
	 * See [MongoDB Query operator: $regex](https://docs.mongodb.com/manual/reference/operator/query/regex/)
	 * @see Criteria.regex
	 */
	fun KProperty<*>.regex(re: String, options: String?) = addOperation { regex(re, options) }

	/**
	 * Syntactical sugar for [isEqualTo] making obvious that we create a regex predicate.
	 * @see Criteria.regex
	 */
	infix fun KProperty<*>.regex(re: Regex) = addOperation { regex(re.toPattern()) }

	/**
	 * Syntactical sugar for [isEqualTo] making obvious that we create a regex predicate.
	 * @see Criteria.regex
	 */
	infix fun KProperty<*>.regex(re: Pattern) = addOperation { regex(re) }

	/**
	 * Syntactical sugar for [isEqualTo] making obvious that we create a regex predicate.
	 * @see Criteria.regex
	 */
	infix fun KProperty<*>.regex(re: BsonRegularExpression) = addOperation { regex(re) }

	/**
	 * Creates a geospatial criterion using a $geoWithin $centerSphere operation. This is only available for
	 * Mongo 2.4 and higher.
	 *
	 * See [MongoDB Query operator:
	 * $geoWithin](https://docs.mongodb.com/manual/reference/operator/query/geoWithin/)
	 *
	 * See [MongoDB Query operator:
	 * $centerSphere](https://docs.mongodb.com/manual/reference/operator/query/centerSphere/)
	 * @see Criteria.withinSphere
	 */
	infix fun KProperty<*>.withinSphere(circle: Circle) = addOperation { withinSphere(circle) }

	/**
	 * Creates a geospatial criterion using a $geoWithin operation.
	 *
	 * See [MongoDB Query operator:
	 * $geoWithin](https://docs.mongodb.com/manual/reference/operator/query/geoWithin/)
	 * @see Criteria.within
	 */
	infix fun KProperty<*>.within(shape: Shape) = addOperation { within(shape) }

	/**
	 * Creates a geospatial criterion using a $near operation.
	 *
	 * See [MongoDB Query operator: $near](https://docs.mongodb.com/manual/reference/operator/query/near/)
	 * @see Criteria.near
	 */
	infix fun KProperty<*>.near(point: Point) = addOperation { near(point) }

	/**
	 * Creates a geospatial criterion using a $nearSphere operation. This is only available for Mongo 1.7 and
	 * higher.
	 *
	 * See [MongoDB Query operator:
	 * $nearSphere](https://docs.mongodb.com/manual/reference/operator/query/nearSphere/)
	 * @see Criteria.nearSphere
	 */
	infix fun KProperty<*>.nearSphere(point: Point) = addOperation { nearSphere(point) }

	/**
	 * Creates criterion using `$geoIntersects` operator which matches intersections of the given `geoJson`
	 * structure and the documents one. Requires MongoDB 2.4 or better.
	 * @see Criteria.intersects
	 */
	infix fun KProperty<*>.intersects(geoJson: GeoJson<*>) = addOperation { intersects(geoJson) }

	/**
	 * Creates a geo-spatial criterion using a $maxDistance operation, for use with $near
	 *
	 * See [MongoDB Query operator:
	 * $maxDistance](https://docs.mongodb.com/manual/reference/operator/query/maxDistance/)
	 * @see Criteria.maxDistance
	 */
	infix fun KProperty<*>.maxDistance(d: Double) = addOperation { maxDistance(d) }

	/**
	 * Creates a geospatial criterion using a $minDistance operation, for use with $near or
	 * $nearSphere.
	 * @see Criteria.minDistance
	 */
	infix fun KProperty<*>.minDistance(d: Double) = addOperation { minDistance(d) }

	/**
	 * Creates a criterion using the $elemMatch operator
	 *
	 * See [MongoDB Query operator:
	 * $elemMatch](https://docs.mongodb.com/manual/reference/operator/query/elemMatch/)
	 * @see Criteria.elemMatch
	 */
	infix fun KProperty<*>.elemMatch(c: Criteria) = addOperation { elemMatch(c) }

	/**
	 * Creates a criterion using the $elemMatch operator
	 *
	 * See [MongoDB Query operator:
	 * $elemMatch](https://docs.mongodb.com/manual/reference/operator/query/elemMatch/)
	 * @see Criteria.elemMatch
	 */
	infix fun KProperty<*>.elemMatch(c: TypedCriteria) = addOperation { elemMatch(typedCriteria(c)) }

	/**
	 * Creates a criterion using the given object as a pattern.
	 * @see Criteria.alike
	 */
	infix fun KProperty<*>.alike(sample: Example<*>) = addOperation { alike(sample) }

	/**
	 * Creates a criterion (`$jsonSchema`) matching documents against a given structure defined by the
	 * [MongoJsonSchema].
	 *
	 * See [MongoDB Query operator:
	 * $jsonSchema](https://docs.mongodb.com/manual/reference/operator/query/jsonSchema/)
	 * @see Criteria.andDocumentStructureMatches
	 */
	infix fun KProperty<*>.andDocumentStructureMatches(schema: MongoJsonSchema) =
		addOperation { andDocumentStructureMatches(schema) }

	/**
	 * Use [BitwiseCriteriaOperators] as gateway to create a criterion using one of the
	 * [bitwise operators](https://docs.mongodb.com/manual/reference/operator/query-bitwise/) like
	 * `$bitsAllClear`.
	 *
	 * Example:
	 * ```
	 * bits { allClear(123) }
	 * ```
	 * @see Criteria.bits
	 */
	infix fun KProperty<*>.bits(bitwiseCriteria: Criteria.BitwiseCriteriaOperators.() -> Criteria) =
		addOperation { bits().let(bitwiseCriteria) }


	private fun <T> KProperty<T>.addOperation(operation: Criteria.() -> Criteria): Operation {
		val typedOperation = Operation(this, operation)
		operations.add(typedOperation)
		return typedOperation
	}

	/**
	 * Creates an 'or' criteria using the $or operator for all of the provided criteria
	 *
	 * Note that mongodb doesn't support an $or operator to be wrapped in a $not operator.
	 * @see Criteria.orOperator
	 */
	fun or(builder: TypedCriteria) = addOperatorWithCriteria(builder, Criteria::orOperator)

	/**
	 * Creates a 'nor' criteria using the $nor operator for all of the provided criteria.
	 *
	 * Note that mongodb doesn't support an $nor operator to be wrapped in a $not operator.
	 * @see Criteria.norOperator
	 */
	fun nor(builder: TypedCriteria) = addOperatorWithCriteria(builder, Criteria::norOperator)

	/**
	 * Creates an 'and' criteria using the $and operator for all of the provided criteria.
	 *
	 * Note that mongodb doesn't support an $and operator to be wrapped in a $not operator.
	 * @see Criteria.andOperator
	 */
	fun and(builder: TypedCriteria) = addOperatorWithCriteria(builder, Criteria::andOperator)

	private fun addOperatorWithCriteria(builder: TypedCriteria, operation: Criteria.(Array<Criteria>) -> Criteria) {
		val otherCriteria = TypedCriteriaBuilder().apply(builder).listCriteria()
		chainCriteria()
		criteria.operation(otherCriteria.toTypedArray())
	}

	/**
	 * Build nested properties.
	 *
	 * Example:
	 * ```
	 * Book::author / Author::name isEqualTo "Herman Melville"
	 * ```
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

	/**
	 * Typed Operation, stores property and operation function.
	 */
	class Operation(
		property: KProperty<Any?>,
		val operation: Criteria.() -> Criteria
	) {
		val name = nestedFieldName(property)
		val criteria by lazy { Criteria(name).operation() }
	}
}