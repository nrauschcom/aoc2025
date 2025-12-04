package shared.multithreaded

import kotlinx.coroutines.*

suspend fun <T> Iterable<T>.forEachParallel(action: suspend (T) -> Unit) =
    coroutineScope {
        map { item ->
            launch(Dispatchers.Default) {
                action(item)
            }
        }.joinAll()
    }

suspend fun <T> Sequence<T>.forEachParallel(action: suspend (T) -> Unit) =
    coroutineScope {
        map { item ->
            launch(Dispatchers.Default) {
                action(item)
            }
        }.toList().joinAll()
    }
