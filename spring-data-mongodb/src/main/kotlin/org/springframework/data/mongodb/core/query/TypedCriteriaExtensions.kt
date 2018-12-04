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

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.find

/**
 * Chain [TypedCriteria].
 *
 * @sample typedCriteriaSample
 *
 * @author Tjeu Kayim
 * @since 2.2
 * @see typedQuery
 */
fun chainCriteria(vararg operations: TypedCriteria): Criteria {
	return operations.fold(Criteria()) { chain, criteria -> chain and criteria }
}

private fun typedCriteriaSample(mongoOperations: MongoOperations) {
	class Author(val name: String)
	class Book(val name: String, val price: Int, val author: Author)
	// Use Property References for field names
	mongoOperations.find<Book>(
		Query(
			Book::name isEqualTo "Moby-Dick"
		)
	)
	// Chain with chainCriteria()
	chainCriteria(
		Book::author elemMatch
			(Author::name isEqualTo "Herman Melville"),
		Book::price exists true
	)
	// Logical operators
	(Book::name isEqualTo "Moby-Dick") and
		(Book::price lt 1200) orOperator (Book::price gt 240)

	// Nested Properties (i.e. refer to "book.author")
	Book::author / Author::name regex "^H"
}
