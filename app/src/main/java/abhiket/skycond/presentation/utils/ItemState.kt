package abhiket.skycond.presentation.utils

sealed interface ItemState<out T> {
    data class Success<out T>(val data: T) : ItemState<T>
    data class Failure<out T>(val data: T, val message: StringValue) : ItemState<T>
    data class Loading<out T>(val data: T) : ItemState<T>
}
