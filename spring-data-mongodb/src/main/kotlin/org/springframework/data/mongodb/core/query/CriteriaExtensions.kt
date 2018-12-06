/*
 * Copyright 2017 the original author or authors.
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
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Point
import org.springframework.data.geo.Shape
import org.springframework.data.mongodb.core.geo.GeoJson
import org.springframework.data.mongodb.core.schema.JsonSchemaObject
import org.springframework.data.mongodb.core.schema.MongoJsonSchema
import java.util.regex.Pattern

/**
 * Extension for [Criteria.is] providing an `isEqualTo` alias since `is` is a reserved keyword in Kotlin.
 *
 * @author Sebastien Deleuze
 * @since 2.0
 */
infix fun Criteria.isEqualTo(o: Any?) : Criteria = `is`(o)

/**
 * Extension for [Criteria.in] providing an `inValues` alias since `in` is a reserved keyword in Kotlin.
 *
 * @author Sebastien Deleuze
 * @since 2.0
 */
infix fun <T: Any?> Criteria.inValues(c: Collection<T>) : Criteria = `in`(c)

/**
 * Extension for [Criteria.in] providing an `inValues` alias since `in` is a reserved keyword in Kotlin.
 *
 * @author Sebastien Deleuze
 * @since 2.0
 */
fun Criteria.inValues(vararg o: Any?) : Criteria = `in`(*o)

/**
 * Creates a criterion using the $ne operator.
 *
 * See [MongoDB Query operator: $ne](https://docs.mongodb.com/manual/reference/operator/query/ne/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.ne
 */
infix fun Criteria.ne(value: Any?): Criteria = ne(value)

/**
 * Creates a criterion using the $lt operator.
 *
 * See [MongoDB Query operator: $lt](https://docs.mongodb.com/manual/reference/operator/query/lt/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.lt
 */
infix fun Criteria.lt(value: Any): Criteria = lt(value)

/**
 * Creates a criterion using the $lte operator.
 *
 * See [MongoDB Query operator: $lte](https://docs.mongodb.com/manual/reference/operator/query/lte/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.lte
 */
infix fun Criteria.lte(value: Any): Criteria = lte(value)

/**
 * Creates a criterion using the $gt operator.
 *
 * See [MongoDB Query operator: $gt](https://docs.mongodb.com/manual/reference/operator/query/gt/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.gt
 */
infix fun Criteria.gt(value: Any): Criteria = gt(value)

/**
 * Creates a criterion using the $gte operator.
 *
 * See [MongoDB Query operator: $gte](https://docs.mongodb.com/manual/reference/operator/query/gte/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.gte
 */
infix fun Criteria.gte(value: Any): Criteria = gte(value)

/**
 * Creates a criterion using the $nin operator.
 *
 * See [MongoDB Query operator: $nin](https://docs.mongodb.com/manual/reference/operator/query/nin/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.nin
 */
infix fun Criteria.nin(value: Collection<*>): Criteria = nin(value)

/**
 * Creates a criterion using the $all operator.
 *
 * See [MongoDB Query operator: $all](https://docs.mongodb.com/manual/reference/operator/query/all/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.all
 */
infix fun Criteria.all(value: Collection<*>): Criteria = all(value)

/**
 * Creates a criterion using the $size operator.
 *
 * See [MongoDB Query operator: $size](https://docs.mongodb.com/manual/reference/operator/query/size/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.size
 */
infix fun Criteria.size(s: Int): Criteria = size(s)

/**
 * Creates a criterion using the $exists operator.
 *
 * See [MongoDB Query operator: $exists](https://docs.mongodb.com/manual/reference/operator/query/exists/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.exists
 */
infix fun Criteria.exists(b: Boolean): Criteria = exists(b)

/**
 * Creates a criterion using the $type operator.
 *
 * See [MongoDB Query operator: $type](https://docs.mongodb.com/manual/reference/operator/query/type/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.type
 */
infix fun Criteria.type(t: Int): Criteria = type(t)

/**
 * Creates a criterion using the $type operator.
 *
 * See [MongoDB Query operator: $type](https://docs.mongodb.com/manual/reference/operator/query/type/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.type
 */
infix fun Criteria.type(t: Collection<JsonSchemaObject.Type>): Criteria = type(*t.toTypedArray())

/**
 * Creates a criterion using a $regex operator.
 *
 * See [MongoDB Query operator: $regex](https://docs.mongodb.com/manual/reference/operator/query/regex/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.regex
 */
infix fun Criteria.regex(re: String): Criteria = regex(re, null)

/**
 * Syntactical sugar for [isEqualTo] making obvious that we create a regex predicate.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.regex
 */
infix fun Criteria.regex(re: Regex): Criteria = regex(re.toPattern())

/**
 * Syntactical sugar for [isEqualTo] making obvious that we create a regex predicate.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.regex
 */
infix fun Criteria.regex(re: Pattern): Criteria = regex(re)

/**
 * Syntactical sugar for [isEqualTo] making obvious that we create a regex predicate.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.regex
 */
infix fun Criteria.regex(re: BsonRegularExpression): Criteria = regex(re)

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
infix fun Criteria.withinSphere(circle: Circle): Criteria = withinSphere(circle)

/**
 * Creates a geospatial criterion using a $geoWithin operation.
 *
 * See [MongoDB Query operator:
	 * $geoWithin](https://docs.mongodb.com/manual/reference/operator/query/geoWithin/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.within
 */
infix fun Criteria.within(shape: Shape): Criteria = within(shape)

/**
 * Creates a geospatial criterion using a $near operation.
 *
 * See [MongoDB Query operator: $near](https://docs.mongodb.com/manual/reference/operator/query/near/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.near
 */
infix fun Criteria.near(point: Point): Criteria = near(point)

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
infix fun Criteria.nearSphere(point: Point): Criteria = nearSphere(point)

/**
 * Creates criterion using `$geoIntersects` operator which matches intersections of the given `geoJson`
 * structure and the documents one. Requires MongoDB 2.4 or better.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.intersects
 */
infix fun Criteria.intersects(geoJson: GeoJson<*>): Criteria = intersects(geoJson)

/**
 * Creates a geo-spatial criterion using a $maxDistance operation, for use with $near
 *
 * See [MongoDB Query operator:
	 * $maxDistance](https://docs.mongodb.com/manual/reference/operator/query/maxDistance/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.maxDistance
 */
infix fun Criteria.maxDistance(d: Double): Criteria = maxDistance(d)

/**
 * Creates a geospatial criterion using a $minDistance operation, for use with $near or
 * $nearSphere.
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.minDistance
 */
infix fun Criteria.minDistance(d: Double): Criteria = minDistance(d)

/**
 * Creates a criterion using the $elemMatch operator
 *
 * See [MongoDB Query operator:
	 * $elemMatch](https://docs.mongodb.com/manual/reference/operator/query/elemMatch/)
 * @author Tjeu Kayim
 * @since 2.2
 * @see Criteria.elemMatch
 */
infix fun Criteria.elemMatch(c: Criteria): Criteria = elemMatch(c)

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
infix fun Criteria.andDocumentStructureMatches(schema: MongoJsonSchema): Criteria =
	andDocumentStructureMatches(schema)

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
infix fun Criteria.bits(bitwiseCriteria: Criteria.BitwiseCriteriaOperators.() -> Criteria) =
	bits().let(bitwiseCriteria)
