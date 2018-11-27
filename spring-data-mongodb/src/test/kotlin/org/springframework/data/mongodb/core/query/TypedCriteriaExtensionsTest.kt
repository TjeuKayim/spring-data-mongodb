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

import example.Author
import example.Book
import org.bson.BsonRegularExpression
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.schema.JsonSchemaObject.Type
import java.util.regex.Pattern

/**
 * @author Tjeu Kayim
 */
class TypedCriteriaExtensionsTest {
	@Test
	fun `Typed query gt and isEqualTo`() {
		val typed = typedCriteria {
			Book::name isEqualTo "Moby-Dick"
			Book::price lt 950
		}
		val classic = Criteria("name").isEqualTo("Moby-Dick")
			.and("price").lt(950)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria isEqualTo`() {
		val typed = typedCriteria { Book::name isEqualTo "Moby-Dick" }
		val classic = Criteria("name").isEqualTo("Moby-Dick")
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria ne`() {
		val typed = typedCriteria { Book::name ne "Moby-Dick" }
		val classic = Criteria("name").ne("Moby-Dick")
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria lt`() {
		val typed = typedCriteria { Book::price lt 100 }
		val classic = Criteria("price").lt(100)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria lte`() {
		val typed = typedCriteria { Book::price lte 100 }
		val classic = Criteria("price").lte(100)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria gt`() {
		val typed = typedCriteria { Book::price gt 100 }
		val classic = Criteria("price").gt(100)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria gte`() {
		val typed = typedCriteria { Book::price gte 100 }
		val classic = Criteria("price").gte(100)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria inValues`() {
		val typed = typedCriteria { Book::price.inValues(1, 2, 3) }
		val classic = Criteria("price").inValues(1, 2, 3)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria inValues list`() {
		val typed = typedCriteria { Book::price inValues listOf(1, 2, 3) }
		val classic = Criteria("price").inValues(listOf(1, 2, 3))
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria nin`() {
		val typed = typedCriteria { Book::price.nin(1, 2, 3) }
		val classic = Criteria("price").nin(1, 2, 3)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria nin list`() {
		val typed = typedCriteria { Book::price nin listOf(1, 2, 3) }
		val classic = Criteria("price").nin(listOf(1, 2, 3))
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria mod`() {
		val typed = typedCriteria { Book::price.mod(2, 3) }
		val classic = Criteria("price").mod(2, 3)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria all`() {
		val typed = typedCriteria { Book::categories.all(1, 2, 3) }
		val classic = Criteria("categories").all(1, 2, 3)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria all list`() {
		val typed = typedCriteria { Book::categories all listOf(1, 2, 3) }
		val classic = Criteria("categories").all(listOf(1, 2, 3))
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria size`() {
		val typed = typedCriteria { Book::categories size 4 }
		val classic = Criteria("categories").size(4)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria exists`() {
		val typed = typedCriteria { Book::name exists true }
		val classic = Criteria("name").exists(true)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria type int`() {
		val typed = typedCriteria { Book::name type 2 }
		val classic = Criteria("name").type(2)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria type array`() {
		val typed = typedCriteria { Book::name type arrayOf(Type.STRING, Type.BOOLEAN) }
		val classic = Criteria("name").type(Type.STRING, Type.BOOLEAN)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria not`() {
		val typed = typedCriteria { Book::name.not() }
		val classic = Criteria("name").not()
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria regex string`() {
		val typed = typedCriteria { Book::name regex "ab+c" }
		val classic = Criteria("name").regex("ab+c")
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria regex string options`() {
		val typed = typedCriteria { Book::name.regex("ab+c", "g") }
		val classic = Criteria("name").regex("ab+c", "g")
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria regex Regex`() {
		val typed = typedCriteria { Book::name regex Regex("ab+c") }
		val classic = Criteria("name").regex(Pattern.compile("ab+c"))
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria regex Pattern`() {
		val typed = typedCriteria { Book::name regex Pattern.compile("ab+c") }
		val classic = Criteria("name").regex(Pattern.compile("ab+c"))
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria regex BsonRegularExpression`() {
		val expression = BsonRegularExpression("ab+c")
		val typed = typedCriteria { Book::name regex expression }
		val classic = Criteria("name").regex(expression)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria withinSphere`() {
		val value = Circle(Point(1.0, 2.0), 3.0)
		val typed = typedCriteria { Book::name withinSphere value }
		val classic = Criteria("name").withinSphere(value)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria within`() {
		val value = Circle(Point(1.0, 2.0), 3.0)
		val typed = typedCriteria { Book::name within value }
		val classic = Criteria("name").within(value)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria near`() {
		val value = Point(1.0, 2.0)
		val typed = typedCriteria { Book::name near value }
		val classic = Criteria("name").near(value)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria nearSphere`() {
		val value = Point(1.0, 2.0)
		val typed = typedCriteria { Book::name nearSphere value }
		val classic = Criteria("name").nearSphere(value)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria intersects`() {
		val value = GeoJsonPoint(1.0, 2.0)
		val typed = typedCriteria { Book::name intersects value }
		val classic = Criteria("name").intersects(value)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria maxDistance`() {
		val typed = typedCriteria { Book::name maxDistance 3.0 }
		val classic = Criteria("name").maxDistance(3.0)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria minDistance`() {
		val typed = typedCriteria { Book::name minDistance 3.0 }
		val classic = Criteria("name").minDistance(3.0)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria elemMatch`() {
		val value = Criteria("price").lt(950)
		val typed = typedCriteria { Book::name elemMatch value }
		val classic = Criteria("name").elemMatch(value)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria elemMatch typedCriteria`() {
		val typed = typedCriteria { Book::name elemMatch { Book::price lt 950 } }
		val classic = Criteria("name").elemMatch(Criteria("price").lt(950))
		assertCriteriaEquals(classic, typed)
	}


	@Test
	fun `Typed criteria or operator`() {
		val typed = typedCriteria {
			Book::name isEqualTo "Moby-Dick"
			or {
				Book::price lt 1200
				Book::price gt 240
			}
		}
		val classic = Criteria("name").isEqualTo("Moby-Dick")
			.orOperator(
				Criteria("price").lt(1200),
				Criteria("price").gt(240)
			)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria nest one level`() {
		val typed = typedCriteria {
			Book::author nest Author::name isEqualTo "Herman Melville"
		}
		val classic = Criteria("author.name").isEqualTo("Herman Melville")
		assertCriteriaEquals(classic, typed)
	}

	private fun assertCriteriaEquals(expected: Criteria, actual: Criteria) {
		assertEquals(expected, actual)
	}
}
