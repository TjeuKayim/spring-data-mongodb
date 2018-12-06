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

import org.assertj.core.api.Assertions.*
import org.bson.BsonRegularExpression
import org.junit.Test
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.schema.JsonSchemaObject.*
import org.springframework.data.mongodb.core.schema.MongoJsonSchema
import java.util.regex.Pattern

/**
 * @author Tjeu Kayim
 */
class TypedCriteriaExtensionsTests {

	@Test
	fun `typedQuery should equal Query`() {

		val classic = Query(
			Criteria()
				.and("price").gt(1100)
				.and("available").isEqualTo(true)
		)
		val typed = Query(where(Book::price) gt 1100 and Book::available isEqualTo true)
		assertThat(classic.queryObject).isEqualTo(typed.queryObject)
	}

	@Test
	fun `chaining gt & isEqualTo() should equal classic criteria`() {

		val typed = where(Book::title) isEqualTo "Moby-Dick" and Book::price lt 950
		val classic = Criteria("title").isEqualTo("Moby-Dick")
			.and("price").lt(950)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `gt and isEqualTo() should equal classic criteria`() {

		val typed = where(Book::title) isEqualTo "Moby-Dick" and
			Book::price lt 950

		val classic = Criteria("title").isEqualTo("Moby-Dick")
			.and("price").lt(950)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `isEqualTo() should equal classic criteria`() {

		val typed = where(Book::title) isEqualTo "Moby-Dick"
		val classic = Criteria("title").isEqualTo("Moby-Dick")
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `ne() should equal classic criteria`() {

		val typed = where(Book::title) ne "Moby-Dick"
		val classic = Criteria("title").ne("Moby-Dick")
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `lt() should equal classic criteria`() {

		val typed = where(Book::price) lt 100
		val classic = Criteria("price").lt(100)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `lte() should equal classic criteria`() {

		val typed = where(Book::price) lte 100
		val classic = Criteria("price").lte(100)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `gt() should equal classic criteria`() {

		val typed = where(Book::price) gt 100
		val classic = Criteria("price").gt(100)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `gte() should equal classic criteria`() {

		val typed = where(Book::price) gte 100
		val classic = Criteria("price").gte(100)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `inValues(vararg) should equal classic criteria`() {

		val typed = where(Book::price).inValues(1, 2, 3)
		val classic = Criteria("price").inValues(1, 2, 3)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `inValues(list) should equal classic criteria`() {

		val typed = where(Book::price) inValues listOf(1, 2, 3)
		val classic = Criteria("price").inValues(listOf(1, 2, 3))
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `nin(vararg) should equal classic criteria`() {

		val typed = where(Book::price).nin(1, 2, 3)
		val classic = Criteria("price").nin(1, 2, 3)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `nin(list) should equal classic criteria`() {

		val typed = where(Book::price) nin listOf(1, 2, 3)
		val classic = Criteria("price").nin(listOf(1, 2, 3))
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `mod() should equal classic criteria`() {

		val typed = where(Book::price).mod(2, 3)
		val classic = Criteria("price").mod(2, 3)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `all(vararg) should equal classic criteria`() {

		val typed = where(Book::categories).all(1, 2, 3)
		val classic = Criteria("categories").all(1, 2, 3)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `all(list) should equal classic criteria`() {

		val typed = where(Book::categories) all listOf(1, 2, 3)
		val classic = Criteria("categories").all(listOf(1, 2, 3))
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `size() should equal classic criteria`() {

		val typed = where(Book::categories) size 4
		val classic = Criteria("categories").size(4)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `exists() should equal classic criteria`() {

		val typed = where(Book::title) exists true
		val classic = Criteria("title").exists(true)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `type(Int) should equal classic criteria`() {

		val typed = where(Book::title) type 2
		val classic = Criteria("title").type(2)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `type(List) should equal classic criteria`() {

		val typed = where(Book::title) type listOf(Type.STRING, Type.BOOLEAN)
		val classic = Criteria("title").type(Type.STRING, Type.BOOLEAN)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `type(vararg) should equal classic criteria`() {

		val typed = where(Book::title).type(Type.STRING, Type.BOOLEAN)
		val classic = Criteria("title").type(Type.STRING, Type.BOOLEAN)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `not() should equal classic criteria`() {

		// TODO: improve not() syntax
		val typed = where(Book::price).not().lt(123)
		val classic = Criteria("price").not().lt(123)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `regex(string) should equal classic criteria`() {

		val typed = where(Book::title) regex "ab+c"
		val classic = Criteria("title").regex("ab+c")
		assertEqualCriteriaByJson(typed, classic)
	}

	@Test
	fun `regex(string, options) should equal classic criteria`() {

		val typed = where(Book::title).regex("ab+c", "g")
		val classic = Criteria("title").regex("ab+c", "g")
		assertEqualCriteriaByJson(typed, classic)
	}

	@Test
	fun `regex(Regex) should equal classic criteria`() {

		val typed = where(Book::title) regex Regex("ab+c")
		val classic = Criteria("title").regex(Pattern.compile("ab+c"))
		assertEqualCriteriaByJson(typed, classic)
	}

	private fun assertEqualCriteriaByJson(typed: Criteria, classic: Criteria) {
		assertThat(typed.criteriaObject.toJson()).isEqualTo(classic.criteriaObject.toJson())
	}

	@Test
	fun `regex(Pattern) should equal classic criteria`() {

		val value = Pattern.compile("ab+c")
		val typed = where(Book::title) regex value
		val classic = Criteria("title").regex(value)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `regex(BsonRegularExpression) should equal classic criteria`() {

		val expression = BsonRegularExpression("ab+c")
		val typed = where(Book::title) regex expression
		val classic = Criteria("title").regex(expression)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `withinSphere() should equal classic criteria`() {

		val value = Circle(Point(1.0, 2.0), 3.0)
		val typed = where(Building::location) withinSphere value
		val classic = Criteria("location").withinSphere(value)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `within() should equal classic criteria`() {

		val value = Circle(Point(1.0, 2.0), 3.0)
		val typed = where(Building::location) within value
		val classic = Criteria("location").within(value)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `near() should equal classic criteria`() {

		val value = Point(1.0, 2.0)
		val typed = where(Building::location) near value
		val classic = Criteria("location").near(value)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `nearSphere() should equal classic criteria`() {

		val value = Point(1.0, 2.0)
		val typed = where(Building::location) nearSphere value
		val classic = Criteria("location").nearSphere(value)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `intersects() should equal classic criteria`() {

		val value = GeoJsonPoint(1.0, 2.0)
		val typed = where(Building::location) intersects value
		val classic = Criteria("location").intersects(value)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `maxDistance() should equal classic criteria`() {

		val typed = where(Building::location) maxDistance 3.0
		val classic = Criteria("location").maxDistance(3.0)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `minDistance() should equal classic criteria`() {

		val typed = where(Building::location) minDistance 3.0
		val classic = Criteria("location").minDistance(3.0)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `elemMatch() should equal classic criteria`() {

		val value = Criteria("price").lt(950)
		val typed = where(Book::title) elemMatch value
		val classic = Criteria("title").elemMatch(value)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `elemMatch(TypedCriteria) should equal classic criteria`() {

		val typed = where(Book::title) elemMatch (where(Book::price) lt 950)
		val classic = Criteria("title").elemMatch(Criteria("price").lt(950))
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `andDocumentStructureMatches() should equal classic criteria`() {

		val value = MongoJsonSchema.builder().required("name").build()
		val typed = where(Book::title) andDocumentStructureMatches value
		val classic = Criteria("title").andDocumentStructureMatches(value)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `bits() should equal classic criteria`() {

		val typed = where(Book::title) bits { allClear(123) }
		val classic = Criteria("title").bits().allClear(123)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `or operator() should equal classic criteria`() {

		val typed = (where(Book::title) isEqualTo "Moby-Dick").orOperator(
			where(Book::price) lt 1200,
			where(Book::price) gt 240
		)
		val classic = Criteria("title").isEqualTo("Moby-Dick")
			.orOperator(
				Criteria("price").lt(1200),
				Criteria("price").gt(240)
			)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `nor() should equal classic criteria`() {

		val typed = (where(Book::title) isEqualTo "Moby-Dick").norOperator(
			where(Book::price) lt 1200,
			where(Book::price) gt 240)
		val classic = Criteria("title").isEqualTo("Moby-Dick")
			.norOperator(
				Criteria("price").lt(1200),
				Criteria("price").gt(240)
			)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `and() should equal classic criteria`() {

		val typed = (where(Book::title) isEqualTo "Moby-Dick").andOperator(
			where(Book::price) lt 1200,
			where(Book::price) gt 240)
		val classic = Criteria("title").isEqualTo("Moby-Dick")
			.andOperator(
				Criteria("price").lt(1200),
				Criteria("price").gt(240)
			)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `infix or & and should equal classic criteria`() {

		val typed = Criteria().andOperator(
			where(Book::title) isEqualTo "Moby-Dick",
			Criteria().orOperator(where(Book::price) lt 1200, where(Book::price) gt 240)
		)
		val classic = Criteria().andOperator(
			Criteria("title").isEqualTo("Moby-Dick"),
			Criteria().orOperator(Criteria("price").lt(1200), Criteria("price").gt(240))
		)
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `One level nested should equal classic criteria`() {

		val typed = where(Book::author / Author::name) isEqualTo "Herman Melville"

		val classic = Criteria("author.name").isEqualTo("Herman Melville")
		assertEqualCriteria(typed, classic)
	}

	@Test
	fun `Two levels nested should equal classic criteria`() {

		data class Entity(val book: Book)

		val typed = where(Entity::book / Book::author / Author::name) isEqualTo "Herman Melville"
		val classic = Criteria("book.author.name").isEqualTo("Herman Melville")
		assertEqualCriteria(typed, classic)
	}

	private fun assertEqualCriteria(typed: Criteria, classic: Criteria) {
		assertThat(typed.criteriaObject).isEqualTo(classic.criteriaObject)
	}

	data class Book(
		val title: String = "Moby-Dick",
		val price: Int = 123,
		val available: Boolean = true,
		val categories: List<String> = emptyList(),
		val author: Author = Author()
	)

	data class Author(
		val name: String = "Herman Melville"
	)

	data class Building(
		val location: GeoJsonPoint = GeoJsonPoint(5.481573, 51.451726)
	)
}
