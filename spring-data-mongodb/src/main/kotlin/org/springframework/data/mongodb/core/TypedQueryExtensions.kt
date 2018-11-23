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

package org.springframework.data.mongodb.core

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults
import org.springframework.data.mongodb.core.query.TypedQuery
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.util.CloseableIterator
import kotlin.reflect.KClass

/**
 * Extension for [MongoOperations.stream] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
inline fun <reified T : Any> MongoOperations.stream(typedQuery: TypedQuery<T>): CloseableIterator<T> =
	stream(typedQuery, T::class.java)

/**
 * Extension for [MongoOperations.stream] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
inline fun <reified T : Any> MongoOperations.stream(typedQuery: TypedQuery<T>, collectionName: String? = null): CloseableIterator<T> =
	if (collectionName != null) stream(typedQuery, T::class.java, collectionName)
	else stream(typedQuery, T::class.java)

/**
 * Extension for [MongoOperations.mapReduce] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
inline fun <reified T : Any> MongoOperations.mapReduce(typedQuery: TypedQuery<T>, collectionName: String, mapFunction: String, reduceFunction: String, options: MapReduceOptions? = null): MapReduceResults<T> =
	if (options != null) mapReduce(typedQuery, collectionName, mapFunction, reduceFunction, options, T::class.java)
	else mapReduce(typedQuery, collectionName, mapFunction, reduceFunction, T::class.java)

/**
 * Extension for [MongoOperations.findOne] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
inline fun <reified T : Any> MongoOperations.findOne(typedQuery: TypedQuery<T>, collectionName: String? = null): T? =
	if (collectionName != null) findOne(typedQuery, T::class.java, collectionName) else findOne(typedQuery, T::class.java)

/**
 * Extension for [MongoOperations.exists] providing a [KClass] based variant.
 *
 * @author Tjeu Kayim
 */
fun <T : Any> MongoOperations.exists(typedQuery: TypedQuery<T>, entityClass: KClass<T>, collectionName: String? = null): Boolean =
	if (collectionName != null) exists(typedQuery, entityClass.java, collectionName)
	else exists(typedQuery, entityClass.java)

/**
 * Extension for [MongoOperations.exists] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
inline fun <reified T : Any> MongoOperations.exists(typedQuery: TypedQuery<T>, collectionName: String? = null): Boolean =
	if (collectionName != null) exists(typedQuery, T::class.java, collectionName)
	else exists(typedQuery, T::class.java)

/**
 * Extension for [MongoOperations.find] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
inline fun <reified T : Any> MongoOperations.find(typedQuery: TypedQuery<T>, collectionName: String? = null): List<T> =
	if (collectionName != null) find(typedQuery, T::class.java, collectionName)
	else find(typedQuery, T::class.java)

/**
 * Extension for [MongoOperations.findDistinct] leveraging reified type parameters.
 *
 * @author Christoph Strobl
 * @since 2.1
 */
inline fun <reified T : Any> MongoOperations.findDistinct(typedQuery: TypedQuery<T>, field: String, entityClass: KClass<*>): List<T> =
	findDistinct(typedQuery, field, entityClass.java, T::class.java)

/**
 * Extension for [MongoOperations.findDistinct] leveraging reified type parameters.
 *
 * @author Christoph Strobl
 * @since 2.1
 */
inline fun <reified T : Any> MongoOperations.findDistinct(typedQuery: TypedQuery<T>, field: String, collectionName: String, entityClass: KClass<*>): List<T> =
	findDistinct(typedQuery, field, collectionName, entityClass.java, T::class.java)

/**
 * Extension for [MongoOperations.findDistinct] leveraging reified type parameters.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.1
 */
inline fun <reified T : Any, reified E : Any> MongoOperations.findDistinct(typedQuery: TypedQuery<T>, field: String, collectionName: String? = null): List<T> =
	if (collectionName != null) findDistinct(typedQuery, field, collectionName, E::class.java, T::class.java)
	else findDistinct(typedQuery, field, E::class.java, T::class.java)

/**
 * Extension for [MongoOperations.findAndModify] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
inline fun <reified T : Any> MongoOperations.findAndModify(typedQuery: TypedQuery<T>, update: Update, options: FindAndModifyOptions, collectionName: String? = null): T? =
	if (collectionName != null) findAndModify(typedQuery, update, options, T::class.java, collectionName)
	else findAndModify(typedQuery, update, options, T::class.java)

/**
 * Extension for [MongoOperations.findAndRemove] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
inline fun <reified T : Any> MongoOperations.findAndRemove(typedQuery: TypedQuery<T>, collectionName: String? = null): T? =
	if (collectionName != null) findAndRemove(typedQuery, T::class.java, collectionName)
	else findAndRemove(typedQuery, T::class.java)

/**
 * Extension for [MongoOperations.count] providing a [KClass] based variant.
 *
 * @author Tjeu Kayim
 */
fun <T : Any> MongoOperations.count(typedQuery: TypedQuery<T>, entityClass: KClass<T>, collectionName: String? = null): Long =
	if (collectionName != null) count(typedQuery, entityClass.java, collectionName)
	else count(typedQuery, entityClass.java)

/**
 * Extension for [MongoOperations.count] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Any> MongoOperations.count(typedQuery: TypedQuery<T>, collectionName: String? = null): Long =
	if (collectionName != null) count(typedQuery, T::class.java, collectionName) else count(typedQuery, T::class.java)

/**
 * Extension for [MongoOperations.upsert] providing a [KClass] based variant.
 *
 * @author Tjeu Kayim
 */
fun <T : Any> MongoOperations.upsert(typedQuery: TypedQuery<T>, update: Update, entityClass: KClass<T>, collectionName: String? = null): UpdateResult =
	if (collectionName != null) upsert(typedQuery, update, entityClass.java, collectionName)
	else upsert(typedQuery, update, entityClass.java)

/**
 * Extension for [MongoOperations.upsert] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Any> MongoOperations.upsert(typedQuery: TypedQuery<T>, update: Update, collectionName: String? = null): UpdateResult =
	if (collectionName != null) upsert(typedQuery, update, T::class.java, collectionName)
	else upsert(typedQuery, update, T::class.java)

/**
 * Extension for [MongoOperations.updateFirst] providing a [KClass] based variant.
 *
 * @author Tjeu Kayim
 */
fun <T : Any> MongoOperations.updateFirst(typedQuery: TypedQuery<T>, update: Update, entityClass: KClass<T>, collectionName: String? = null): UpdateResult =
	if (collectionName != null) updateFirst(typedQuery, update, entityClass.java, collectionName)
	else updateFirst(typedQuery, update, entityClass.java)

/**
 * Extension for [MongoOperations.updateFirst] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Any> MongoOperations.updateFirst(typedQuery: TypedQuery<T>, update: Update, collectionName: String? = null): UpdateResult =
	if (collectionName != null) updateFirst(typedQuery, update, T::class.java, collectionName)
	else updateFirst(typedQuery, update, T::class.java)

/**
 * Extension for [MongoOperations.updateMulti] providing a [KClass] based variant.
 *
 * @author Tjeu Kayim
 */
fun <T : Any> MongoOperations.updateMulti(typedQuery: TypedQuery<T>, update: Update, entityClass: KClass<T>, collectionName: String? = null): UpdateResult =
	if (collectionName != null) updateMulti(typedQuery, update, entityClass.java, collectionName)
	else updateMulti(typedQuery, update, entityClass.java)

/**
 * Extension for [MongoOperations.updateMulti] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Any> MongoOperations.updateMulti(typedQuery: TypedQuery<T>, update: Update, collectionName: String? = null): UpdateResult =
	if (collectionName != null) updateMulti(typedQuery, update, T::class.java, collectionName)
	else updateMulti(typedQuery, update, T::class.java)

/**
 * Extension for [MongoOperations.remove] providing a [KClass] based variant.
 *
 * @author Tjeu Kayim
 */
fun <T : Any> MongoOperations.remove(typedQuery: TypedQuery<T>, entityClass: KClass<T>, collectionName: String? = null): DeleteResult =
	if (collectionName != null) remove(typedQuery, entityClass.java, collectionName)
	else remove(typedQuery, entityClass.java)

/**
 * Extension for [MongoOperations.remove] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Any> MongoOperations.remove(typedQuery: TypedQuery<T>, collectionName: String? = null): DeleteResult =
	if (collectionName != null) remove(typedQuery, T::class.java, collectionName)
	else remove(typedQuery, T::class.java)

/**
 * Extension for [MongoOperations.findAllAndRemove] leveraging reified type parameters.
 *
 * @author Tjeu Kayim
 */
inline fun <reified T : Any> MongoOperations.findAllAndRemove(typedQuery: TypedQuery<T>): List<T> =
	findAllAndRemove(typedQuery, T::class.java)
