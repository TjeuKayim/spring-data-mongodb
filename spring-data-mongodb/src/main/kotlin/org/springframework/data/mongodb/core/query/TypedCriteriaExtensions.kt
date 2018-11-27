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

/**
 * Typed criteria builder.
 *
 * @see typedQuery
 * @sample typedCriteriaSample
 * @author Tjeu Kayim
 */
fun typedCriteria(criteria: TypedCriteria): Criteria {
	val builder = TypedCriteriaBuilder().apply(criteria)
	return builder.chainCriteria()
}

/**
 * Create a new Query.
 *
 * @see typedCriteria
 * @author Tjeu Kayim
 */
fun typedQuery(criteria: TypedCriteria): Query {
	return Query(typedCriteria(criteria))
}

private fun typedCriteriaSample() {
	class Author(val name: String)
	class Book(val name: String, val price: Int, val author: Author)
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
	// Nested properties
	typedCriteria {
		Book::author / Author::name regex "^H"
	}
}
