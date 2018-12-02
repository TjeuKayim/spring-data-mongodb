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

import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/**
 * Nested property to generate Mongo field name.
 */
class NestedProperty<T, U>(
	internal val parent: KProperty<T>,
	internal val child: KProperty1<T, U>
) : KProperty<U> by child

fun nestedFieldName(property: KProperty<*>): String {
	return when (property) {
		is NestedProperty<*, *> ->
			"${nestedFieldName(property.parent)}.${property.child.name}"
		else -> property.name
	}
}
