@file:JvmName("FlowQuery")

package abhiket.skycond.uitls.extensions

import app.cash.sqldelight.Query
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

/** Turns this [Query] into a [Flow] which emits whenever the underlying result set changes. */
@JvmName("toFlow")
fun <T : Any> Query<T>.asFlow(): Flow<Query<T>> = flow {
    val channel = Channel<Unit>(CONFLATED)
    channel.trySend(Unit)

    val listener = Query.Listener {
        channel.trySend(Unit)
    }
    addListener(listener)
    try {
        for (item in channel) {
            emit(this@asFlow)
        }
    } finally {
        removeListener(listener)
    }
}

fun <T : Any> Flow<Query<T>>.mapToOne(
    context: CoroutineContext,
): Flow<T> = map {
    withContext(context) {
        it.awaitAsOne()
    }
}

fun <T : Any> Flow<Query<T>>.mapToOneOrDefault(
    defaultValue: T,
    context: CoroutineContext,
): Flow<T> = map {
    withContext(context) {
        it.awaitAsOneOrNull() ?: defaultValue
    }
}

fun <T : Any> Flow<Query<T>>.mapToOneOrNull(
    context: CoroutineContext,
): Flow<T?> = map {
    withContext(context) {
        it.awaitAsOneOrNull()
    }
}

fun <T : Any> Flow<Query<T>>.mapToOneNotNull(
    context: CoroutineContext,
): Flow<T> = mapNotNull {
    withContext(context) {
        it.awaitAsOneOrNull()
    }
}

fun <T : Any> Flow<Query<T>>.mapToList(
    context: CoroutineContext,
): Flow<List<T>> = map {
    withContext(context) {
        it.awaitAsList()
    }
}
