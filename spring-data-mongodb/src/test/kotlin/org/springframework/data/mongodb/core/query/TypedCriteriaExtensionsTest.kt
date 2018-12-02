/*
 * Copyright 2018 the original author or authors.
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
import org.assertj.core.api.Assertions.*
import org.bson.BsonRegularExpression
import org.junit.Test
import org.springframework.data.domain.Example
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.schema.JsonSchemaObject.*
import org.springframework.data.mongodb.core.schema.MongoJsonSchema
import java.util.regex.Pattern

/**
 * @author Tjeu Kayim
 */
class TypedCriteriaExtensionsUnitTests {

	@Test
	fun `typedQuery should equal Query`() {

		val classic = Query(
			Criteria()
				.and("price").gt(1100)
				.and("available").isEqualTo(true)
		)
		val typed = typedQuery {
			Book::price gt 1100
			Book::available isEqualTo true
		}
		assertThat(classic.queryObject).isEqualTo(typed.queryObject)
	}

	@Test
	fun `gt and isEqualTo() typed criteria should equal classic criteria`() {

		val typed = typedCriteria {
			Book::name isEqualTo "Moby-Dick"
			Book::price lt 950
		}
		val classic = Criteria("name").isEqualTo("Moby-Dick")
			.and("price").lt(950)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `isEqualTo() typed criteria should equal classic criteria`() {

		val typed = typedCriteria { Book::name isEqualTo "Moby-Dick" }
		val classic = Criteria("name").isEqualTo("Moby-Dick")
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `ne() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name ne "Moby-Dick" }
		val classic = Criteria("name").ne("Moby-Dick")
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `lt() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::price lt 100 }
		val classic = Criteria("price").lt(100)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `lte() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::price lte 100 }
		val classic = Criteria("price").lte(100)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `gt() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::price gt 100 }
		val classic = Criteria("price").gt(100)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `gte() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::price gte 100 }
		val classic = Criteria("price").gte(100)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `inValues(vararg) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::price.inValues(1, 2, 3) }
		val classic = Criteria("price").inValues(1, 2, 3)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `inValues(list) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::price inValues listOf(1, 2, 3) }
		val classic = Criteria("price").inValues(listOf(1, 2, 3))
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `nin(vararg) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::price.nin(1, 2, 3) }
		val classic = Criteria("price").nin(1, 2, 3)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `nin(list) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::price nin listOf(1, 2, 3) }
		val classic = Criteria("price").nin(listOf(1, 2, 3))
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `mod() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::price.mod(2, 3) }
		val classic = Criteria("price").mod(2, 3)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `all(vararg) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::categories.all(1, 2, 3) }
		val classic = Criteria("categories").all(1, 2, 3)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `all(list) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::categories all listOf(1, 2, 3) }
		val classic = Criteria("categories").all(listOf(1, 2, 3))
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `size() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::categories size 4 }
		val classic = Criteria("categories").size(4)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `exists() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name exists true }
		val classic = Criteria("name").exists(true)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `type(Int) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name type 2 }
		val classic = Criteria("name").type(2)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `type(Array) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name type arrayOf(Type.STRING, Type.BOOLEAN) }
		val classic = Criteria("name").type(Type.STRING, Type.BOOLEAN)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `not() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name.not() }
		val classic = Criteria("name").not()
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `regex(string) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name regex "ab+c" }
		val classic = Criteria("name").regex("ab+c")
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `regex(string, options) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name.regex("ab+c", "g") }
		val classic = Criteria("name").regex("ab+c", "g")
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `regex(Regex) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name regex Regex("ab+c") }
		val classic = Criteria("name").regex(Pattern.compile("ab+c"))
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `regex(Pattern) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name regex Pattern.compile("ab+c") }
		val classic = Criteria("name").regex(Pattern.compile("ab+c"))
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `regex(BsonRegularExpression) typed criteria should equal classic criteria`() {
		
		val expression = BsonRegularExpression("ab+c")
		val typed = typedCriteria { Book::name regex expression }
		val classic = Criteria("name").regex(expression)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `withinSphere() typed criteria should equal classic criteria`() {
		
		val value = Circle(Point(1.0, 2.0), 3.0)
		val typed = typedCriteria { Book::name withinSphere value }
		val classic = Criteria("name").withinSphere(value)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `within() typed criteria should equal classic criteria`() {
		
		val value = Circle(Point(1.0, 2.0), 3.0)
		val typed = typedCriteria { Book::name within value }
		val classic = Criteria("name").within(value)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `near() typed criteria should equal classic criteria`() {
		
		val value = Point(1.0, 2.0)
		val typed = typedCriteria { Book::name near value }
		val classic = Criteria("name").near(value)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `nearSphere() typed criteria should equal classic criteria`() {
		
		val value = Point(1.0, 2.0)
		val typed = typedCriteria { Book::name nearSphere value }
		val classic = Criteria("name").nearSphere(value)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `intersects() typed criteria should equal classic criteria`() {
		
		val value = GeoJsonPoint(1.0, 2.0)
		val typed = typedCriteria { Book::name intersects value }
		val classic = Criteria("name").intersects(value)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `maxDistance() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name maxDistance 3.0 }
		val classic = Criteria("name").maxDistance(3.0)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `minDistance() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name minDistance 3.0 }
		val classic = Criteria("name").minDistance(3.0)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `elemMatch() typed criteria should equal classic criteria`() {
		
		val value = Criteria("price").lt(950)
		val typed = typedCriteria { Book::name elemMatch value }
		val classic = Criteria("name").elemMatch(value)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `elemMatch(TypedCriteria) typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name elemMatch { Book::price lt 950 } }
		val classic = Criteria("name").elemMatch(Criteria("price").lt(950))
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `alike() typed criteria should equal classic criteria`() {
		
		val value = Example.of(Book())
		val typed = typedCriteria { Book::name alike value }
		val classic = Criteria("name").alike(value)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `andDocumentStructureMatches() typed criteria should equal classic criteria`() {
		
		val value = MongoJsonSchema.builder().required("name").build()
		val typed = typedCriteria { Book::name andDocumentStructureMatches value }
		val classic = Criteria("name").andDocumentStructureMatches(value)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `bits() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria { Book::name bits { allClear(123) } }
		val classic = Criteria("name").bits().allClear(123)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `or operator() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria {
			Book::name isEqualTo "Moby-Dick"
			or(
				{ Book::price lt 1200 },
				{ Book::price gt 240 }
			)
		}
		val classic = Criteria("name").isEqualTo("Moby-Dick")
			.orOperator(
				Criteria("price").lt(1200),
				Criteria("price").gt(240)
			)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `nor() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria {
			Book::name isEqualTo "Moby-Dick"
			nor(
				{ Book::price lt 1200 },
				{ Book::price gt 240 }
			)
		}
		val classic = Criteria("name").isEqualTo("Moby-Dick")
			.norOperator(
				Criteria("price").lt(1200),
				Criteria("price").gt(240)
			)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `and() typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria {
			Book::name isEqualTo "Moby-Dick"
			and(
				{ Book::price lt 1200 },
				{ Book::price gt 240 }
			)
		}
		val classic = Criteria("name").isEqualTo("Moby-Dick")
			.andOperator(
				Criteria("price").lt(1200),
				Criteria("price").gt(240)
			)
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `One level nested typed criteria should equal classic criteria`() {
		
		val typed = typedCriteria {
			Book::author / Author::name isEqualTo "Herman Melville"
		}
		val classic = Criteria("author.name").isEqualTo("Herman Melville")
		assertThat(typed).isEqualTo(classic)
	}

	@Test
	fun `Two levels nested typed criteria should equal classic criteria`() {
		
		data class Entity(val book: Book)

		val typed = typedCriteria {
			Entity::book / Book::author / Author::name isEqualTo "Herman Melville"
		}
		val classic = Criteria("book.author.name").isEqualTo("Herman Melville")
		assertThat(typed).isEqualTo(classic)
	}

}
