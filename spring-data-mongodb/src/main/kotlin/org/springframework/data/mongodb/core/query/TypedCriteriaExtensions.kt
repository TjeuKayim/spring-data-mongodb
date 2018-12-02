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

/**
 * Build [Criteria] with Property References as field names.
 *
 * @sample typedCriteriaSample
 *
 * @author Tjeu Kayim
 * @since 2.2
 * @see typedQuery
 */
fun typedCriteria(criteria: TypedCriteria): Criteria =
	TypedCriteriaBuilder().apply(criteria).criteria

/**
 * Shorthand for `Query(typedCriteria())`.
 *
 * @author Tjeu Kayim
 * @since 2.2
 * @see typedCriteria
 */
fun typedQuery(criteria: TypedCriteria): Query =
	Query(typedCriteria(criteria))

private fun typedCriteriaSample() {
	class Author(val name: String)
	class Book(val name: String, val price: Int, val author: Author)
	// Use Property References for field names
	typedCriteria {
		Book::name isEqualTo "Moby-Dick"
		Book::price exists true
	}
	// $or, $nor, $and operators
	typedCriteria {
		Book::name isEqualTo "Moby-Dick"
		or(
			{ Book::price lt 1200 },
			{ Book::price gt 240 }
		)
	}
	// Nested Properties (i.e. refer to "book.author")
	typedCriteria {
		Book::author / Author::name regex "^H"
	}
}
