package abhiket.skycond.data.utils

import androidx.lifecycle.LiveData
import app.cash.sqldelight.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


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

@JvmName("toLiveData")
fun <T : Any> Query<T>.asLiveData(scope: CoroutineScope): LiveData<T> = object : LiveData<T>() {
    val listener = Query.Listener {
        scope.launch {
            postValue(executeAsOneOrNull())
        }
    }

    override fun onActive() {
        addListener(listener)
        scope.launch {
            postValue(executeAsOneOrNull())
        }
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
        removeListener(listener)
    }
}

@JvmName("toListLiveData")
fun <T : Any> Query<T>.asListLiveData(
    scope: CoroutineScope
): LiveData<List<T>> = object : LiveData<List<T>>() {
    val listener = Query.Listener {
        scope.launch {
            postValue(executeAsList())
        }
    }

    override fun onActive() {
        addListener(listener)
        scope.launch {
            postValue(executeAsList())
        }
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
        removeListener(listener)
    }
}



