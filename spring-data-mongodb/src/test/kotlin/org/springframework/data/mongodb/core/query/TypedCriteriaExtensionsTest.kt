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

import org.bson.types.ObjectId
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.data.mongodb.core.mapping.Document

/**
 * @author Tjeu Kayim
 */
class TypedCriteriaExtensionsTest {
	@Test
	fun `Typed query gt and isEqualTo`() {
		val typed = typedCriteria {
			Book::price gt 1100
			Book::available isEqualTo true
		}
		val classic = Criteria("price").gt(1100)
			.and("available").isEqualTo(true)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria isEqualTo`() {
		val typed = typedCriteria {Book::name isEqualTo "Moby-Dick"}
		val classic = Criteria("name").isEqualTo("Moby-Dick")
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria ne`() {
		val typed = typedCriteria {Book::name ne "Moby-Dick"}
		val classic = Criteria("name").ne("Moby-Dick")
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria lt`() {
		val typed = typedCriteria {Book::price lt 100}
		val classic = Criteria("price").lt(100)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria lte`() {
		val typed = typedCriteria {Book::price lte 100}
		val classic = Criteria("price").lte(100)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria gt`() {
		val typed = typedCriteria {Book::price gt 100}
		val classic = Criteria("price").gt(100)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria gte`() {
		val typed = typedCriteria {Book::price gte 100}
		val classic = Criteria("price").gte(100)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria inValues`() {
		val typed = typedCriteria {Book::price.inValues(1, 2, 3)}
		val classic = Criteria("price").inValues(1, 2, 3)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria inValues list`() {
		val typed = typedCriteria {Book::price inValues listOf(1, 2, 3)}
		val classic = Criteria("price").inValues(listOf(1, 2, 3))
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria nin`() {
		val typed = typedCriteria {Book::price.nin(1, 2, 3)}
		val classic = Criteria("price").nin(1, 2, 3)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria nin list`() {
		val typed = typedCriteria {Book::price nin listOf(1, 2, 3)}
		val classic = Criteria("price").nin(listOf(1, 2, 3))
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria mod`() {
		val typed = typedCriteria {Book::price.mod(2, 3)}
		val classic = Criteria("price").mod(2, 3)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria all`() {
		val typed = typedCriteria {Book::authors.all(1, 2, 3)}
		val classic = Criteria("authors").all(1, 2, 3)
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria all list`() {
		val typed = typedCriteria {Book::authors all listOf(1, 2, 3)}
		val classic = Criteria("authors").all(listOf(1, 2, 3))
		assertCriteriaEquals(classic, typed)
	}

	@Test
	fun `Typed criteria size`() {
		val typed = typedCriteria {Book::authors size 4}
		val classic = Criteria("authors").size(4)
		assertCriteriaEquals(classic, typed)
	}

	private fun assertCriteriaEquals(expected: CriteriaDefinition, actual: CriteriaDefinition) {
		assertEquals(expected.criteriaObject, actual.criteriaObject)
	}
}

@Document("books")
data class Book(
	val id: ObjectId,
	val name: String,
	val price: Int,
	val available: Boolean,
	val authors: List<Author>
)

data class Author(
	val id: ObjectId,
	val name: String,
	val price: Int,
	val available: Boolean
)
