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

/**
 * Creates a criterion using the given object as a pattern.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.alike
 */
fun alike(sample: Example<*>) = TypedCriteria { alike(sample) }

/**
 * Wrap [Criteria] method with type-safe field name.
 */
private fun KProperty<*>.operator(operation: Criteria.() -> Unit): TypedCriteria {
	val key = nestedFieldName(this)
	return TypedCriteria(operation, key)
}

/**
 * Creates a criterion using equality.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.isEqualTo
 */
infix fun <T> KProperty<T>.isEqualTo(value: T) = operator { isEqualTo(value) }

/**
 * Creates a criterion using the $ne operator.
 *
 * See [MongoDB Query operator: $ne](https://docs.mongodb.com/manual/reference/operator/query/ne/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.ne
 */
infix fun <T> KProperty<T>.ne(value: T) = operator { ne(value) }

/**
 * Creates a criterion using the $lt operator.
 *
 * See [MongoDB Query operator: $lt](https://docs.mongodb.com/manual/reference/operator/query/lt/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.lt
 */
infix fun <T> KProperty<T>.lt(value: T) = operator { lt(value) }

/**
 * Creates a criterion using the $lte operator.
 *
 * See [MongoDB Query operator: $lte](https://docs.mongodb.com/manual/reference/operator/query/lte/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.lte
 */
infix fun <T> KProperty<T>.lte(value: T) = operator { lte(value) }

/**
 * Creates a criterion using the $gt operator.
 *
 * See [MongoDB Query operator: $gt](https://docs.mongodb.com/manual/reference/operator/query/gt/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.gt
 */
infix fun <T> KProperty<T>.gt(value: T) = operator { gt(value) }

/**
 * Creates a criterion using the $gte operator.
 *
 * See [MongoDB Query operator: $gte](https://docs.mongodb.com/manual/reference/operator/query/gte/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.gte
 */
infix fun <T> KProperty<T>.gte(value: T) = operator { gte(value) }

/**
 * Creates a criterion using the $in operator.
 *
 * See [MongoDB Query operator: $in](https://docs.mongodb.com/manual/reference/operator/query/in/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.inValues
 */
fun <T> KProperty<T>.inValues(vararg o: Any) = operator { `in`(*o) }

/**
 * Creates a criterion using the $in operator.
 *
 * See [MongoDB Query operator: $in](https://docs.mongodb.com/manual/reference/operator/query/in/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.inValues
 */
infix fun <T> KProperty<T>.inValues(value: Collection<T>) = operator { `in`(value) }

/**
 * Creates a criterion using the $nin operator.
 *
 * See [MongoDB Query operator: $nin](https://docs.mongodb.com/manual/reference/operator/query/nin/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.nin
 */
fun <T> KProperty<T>.nin(vararg o: Any) = operator { nin(*o) }

/**
 * Creates a criterion using the $nin operator.
 *
 * See [MongoDB Query operator: $nin](https://docs.mongodb.com/manual/reference/operator/query/nin/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.nin
 */
infix fun <T> KProperty<T>.nin(value: Collection<T>) = operator { nin(value) }

/**
 * Creates a criterion using the $mod operator.
 *
 * See [MongoDB Query operator: $mod](https://docs.mongodb.com/manual/reference/operator/query/mod/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.mod
 */
fun KProperty<Number>.mod(value: Number, remainder: Number) = operator { mod(value, remainder) }

/**
 * Creates a criterion using the $all operator.
 *
 * See [MongoDB Query operator: $all](https://docs.mongodb.com/manual/reference/operator/query/all/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.all
 */
fun KProperty<*>.all(vararg o: Any) = operator { all(*o) }

/**
 * Creates a criterion using the $all operator.
 *
 * See [MongoDB Query operator: $all](https://docs.mongodb.com/manual/reference/operator/query/all/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.all
 */
infix fun KProperty<*>.all(value: Collection<*>) = operator { all(value) }

/**
 * Creates a criterion using the $size operator.
 *
 * See [MongoDB Query operator: $size](https://docs.mongodb.com/manual/reference/operator/query/size/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.size
 */
infix fun KProperty<*>.size(s: Int) = operator { size(s) }

/**
 * Creates a criterion using the $exists operator.
 *
 * See [MongoDB Query operator: $exists](https://docs.mongodb.com/manual/reference/operator/query/exists/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.exists
 */
infix fun KProperty<*>.exists(b: Boolean) = operator { exists(b) }

/**
 * Creates a criterion using the $type operator.
 *
 * See [MongoDB Query operator: $type](https://docs.mongodb.com/manual/reference/operator/query/type/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.type
 */
infix fun KProperty<*>.type(t: Int) = operator { type(t) }

/**
 * Creates a criterion using the $type operator.
 *
 * See [MongoDB Query operator: $type](https://docs.mongodb.com/manual/reference/operator/query/type/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.type
 */
infix fun KProperty<*>.type(t: Collection<JsonSchemaObject.Type>) = operator { type(*t.toTypedArray()) }

/**
 * Creates a criterion using the $type operator.
 *
 * See [MongoDB Query operator: $type](https://docs.mongodb.com/manual/reference/operator/query/type/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.type
 */
fun KProperty<*>.type(vararg t: JsonSchemaObject.Type) = operator { type(*t) }

/**
 * Creates a criterion using the $not meta operator which affects the clause directly following
 *
 * See [MongoDB Query operator: $not](https://docs.mongodb.com/manual/reference/operator/query/not/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.not
 */
fun KProperty<*>.not() = operator { not() }

/**
 * Creates a criterion using a $regex operator.
 *
 * See [MongoDB Query operator: $regex](https://docs.mongodb.com/manual/reference/operator/query/regex/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.regex
 */
infix fun KProperty<String?>.regex(re: String) = operator { regex(re, null) }

/**
 * Creates a criterion using a $regex and $options operator.
 *
 * See [MongoDB Query operator: $regex](https://docs.mongodb.com/manual/reference/operator/query/regex/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.regex
 */
fun KProperty<String?>.regex(re: String, options: String?) = operator { regex(re, options) }

/**
 * Syntactical sugar for [isEqualTo] making obvious that we create a regex predicate.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.regex
 */
infix fun KProperty<String?>.regex(re: Regex) = operator { regex(re.toPattern()) }

/**
 * Syntactical sugar for [isEqualTo] making obvious that we create a regex predicate.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.regex
 */
infix fun KProperty<String?>.regex(re: Pattern) = operator { regex(re) }

/**
 * Syntactical sugar for [isEqualTo] making obvious that we create a regex predicate.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.regex
 */
infix fun KProperty<String?>.regex(re: BsonRegularExpression) = operator { regex(re) }

/**
 * Creates a geospatial criterion using a $geoWithin $centerSphere operation. This is only available for
 * Mongo 2.4 and higher.
 *
 * See [MongoDB Query operator:
	 * $geoWithin](https://docs.mongodb.com/manual/reference/operator/query/geoWithin/)
 *
 * See [MongoDB Query operator:
	 * $centerSphere](https://docs.mongodb.com/manual/reference/operator/query/centerSphere/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.withinSphere
 */
infix fun KProperty<GeoJson<*>>.withinSphere(circle: Circle) = operator { withinSphere(circle) }

/**
 * Creates a geospatial criterion using a $geoWithin operation.
 *
 * See [MongoDB Query operator:
	 * $geoWithin](https://docs.mongodb.com/manual/reference/operator/query/geoWithin/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.within
 */
infix fun KProperty<GeoJson<*>>.within(shape: Shape) = operator { within(shape) }

/**
 * Creates a geospatial criterion using a $near operation.
 *
 * See [MongoDB Query operator: $near](https://docs.mongodb.com/manual/reference/operator/query/near/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.near
 */
infix fun KProperty<GeoJson<*>>.near(point: Point) = operator { near(point) }

/**
 * Creates a geospatial criterion using a $nearSphere operation. This is only available for Mongo 1.7 and
 * higher.
 *
 * See [MongoDB Query operator:
	 * $nearSphere](https://docs.mongodb.com/manual/reference/operator/query/nearSphere/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.nearSphere
 */
infix fun KProperty<GeoJson<*>>.nearSphere(point: Point) = operator { nearSphere(point) }

/**
 * Creates criterion using `$geoIntersects` operator which matches intersections of the given `geoJson`
 * structure and the documents one. Requires MongoDB 2.4 or better.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.intersects
 */
infix fun KProperty<GeoJson<*>>.intersects(geoJson: GeoJson<*>) = operator { intersects(geoJson) }

/**
 * Creates a geo-spatial criterion using a $maxDistance operation, for use with $near
 *
 * See [MongoDB Query operator:
	 * $maxDistance](https://docs.mongodb.com/manual/reference/operator/query/maxDistance/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.maxDistance
 */
infix fun KProperty<GeoJson<*>>.maxDistance(d: Double) = operator { maxDistance(d) }

/**
 * Creates a geospatial criterion using a $minDistance operation, for use with $near or
 * $nearSphere.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.minDistance
 */
infix fun KProperty<GeoJson<*>>.minDistance(d: Double) = operator { minDistance(d) }

/**
 * Creates a criterion using the $elemMatch operator
 *
 * See [MongoDB Query operator:
	 * $elemMatch](https://docs.mongodb.com/manual/reference/operator/query/elemMatch/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.elemMatch
 */
infix fun KProperty<*>.elemMatch(c: Criteria) = operator { elemMatch(c) }

/**
 * Creates a criterion (`$jsonSchema`) matching documents against a given structure defined by the
 * [MongoJsonSchema].
 *
 * See [MongoDB Query operator:
	 * $jsonSchema](https://docs.mongodb.com/manual/reference/operator/query/jsonSchema/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.andDocumentStructureMatches
 */
infix fun KProperty<*>.andDocumentStructureMatches(schema: MongoJsonSchema) =
	operator { andDocumentStructureMatches(schema) }

/**
 * Use [Criteria.BitwiseCriteriaOperators] as gateway to create a criterion using one of the
 * [bitwise operators](https://docs.mongodb.com/manual/reference/operator/query-bitwise/) like
 * `$bitsAllClear`.
 *
 * Example:
 * ```
 * bits { allClear(123) }
 * ```
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.bits
 */
infix fun KProperty<*>.bits(bitwiseCriteria: Criteria.BitwiseCriteriaOperators.() -> Criteria) =
	operator { bits().let(bitwiseCriteria) }

/**
 * Build TypedCriteria for logical operators (or, nor & and).
 */
private fun logicalOperator(
	operation: Criteria.(Array<out Criteria>) -> Criteria,
	vararg builders: Criteria
) = TypedCriteria { operation(builders) }

/**
 * Creates an 'or' criteria using the $or operator for all of the provided criteria
 *
 * Note that mongodb doesn't support an $or operator to be wrapped in a $not operator.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.orOperator
 */
fun orOperator(vararg builders: Criteria) = logicalOperator(Criteria::orOperator, *builders)

/**
 * Creates a 'nor' criteria using the $nor operator for all of the provided criteria.
 *
 * Note that mongodb doesn't support an $nor operator to be wrapped in a $not operator.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.norOperator
 */
fun norOperator(vararg builders: Criteria) = logicalOperator(Criteria::norOperator, *builders)

/**
 * Creates an 'and' criteria using the $and operator for all of the provided criteria.
 *
 * Note that mongodb doesn't support an $and operator to be wrapped in a $not operator.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.andOperator
 */
fun andOperator(vararg builders: Criteria) = logicalOperator(Criteria::andOperator, *builders)

/**
 * Creates an 'or' criteria using the $or operator for all of the provided criteria
 *
 * Note that mongodb doesn't support an $or operator to be wrapped in a $not operator.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.orOperator
 */
infix fun Criteria.orOperator(other: Criteria) = logicalOperator(Criteria::orOperator, this, other)

/**
 * Creates a 'nor' criteria using the $nor operator for all of the provided criteria.
 *
 * Note that mongodb doesn't support an $nor operator to be wrapped in a $not operator.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.norOperator
 */
infix fun Criteria.norOperator(other: Criteria) = logicalOperator(Criteria::norOperator, this, other)

/**
 * Creates an 'and' criteria using the $and operator for all of the provided criteria.
 *
 * Note that mongodb doesn't support an $and operator to be wrapped in a $not operator.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.andOperator
 */
infix fun Criteria.andOperator(other: Criteria) = logicalOperator(Criteria::andOperator, this, other)
