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

import com.nhaarman.mockito_kotlin.mock
import org.bson.types.ObjectId
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.mapping.Document

/**
 * @author Tjeu Kayim
 */
class TypedQueryTest {

	val operations: MongoOperations = mock()

	@Test
	fun `Typed query gt and isEqualTo`() {
		val classic = Criteria("price").gt(1100)
			.and("available").isEqualTo(true)

		val typed = typedCriteria<Book> {
			Book::price gt 1100
			Book::available isEqualTo true
		}

		assertEquals(classic.criteriaObject, typed.criteriaObject)
	}

	@Test
	fun `MongoOperations find by typed query`() {
		operations.find<Book>(typedQuery { Book::price gt 1100 })
	}

	@Test
	fun `Test TypedQuery`() {
		val classic = Query(
			Criteria()
				.and("price").gt(1100)
				.and("available").isEqualTo(true)
		)

		val typed = typedQuery<Book> {
			Book::price gt 1100
			Book::available isEqualTo true
		}

		assertEquals(classic.queryObject, typed.queryObject)
	}

	@Test
	fun `TypedQuery addCriteria`() {
		TypedQuery<Book>().addTypedCriteria(Book::price gt 1100)
//		operations.find<Book>(TypedQuery().addTypedCriteria(typedCriteria {  }))
	}

	@Test
	fun `Typed update`() {
//		operations.updateFirst(Book::name isEqualTo "Moby-Dick", set(Book::price, 11))
	}

	@Test
	fun `Nested query`() {

	}
}

@Document("books")
data class Book(
	val id: ObjectId,
	val name: String,
	val price: Int,
	val available: Boolean,
	val author: Author
)

data class Author(
	val id: ObjectId,
	val name: String,
	val price: Int,
	val available: Boolean
)
