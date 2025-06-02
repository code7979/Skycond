package abhiket.skycond.presentation.utils

//sealed interface ItemState<out T> {
//    data class Success<out T>(val data: T) : ItemState<T>
//    data class Failure<out T>(val data: T, val message: StringValue) : ItemState<T>
//    data class Loading<out T>(val data: T) : ItemState<T>
//}

sealed class ItemState<out T>(val position: Int, val data: T? = null) {
    class Loading(position: Int) : ItemState<Nothing>(position, null)
    class Success<out T>(position: Int, data: T) : ItemState<T>(position, data)
    class Failure(position: Int, val stringValue: StringValue) : ItemState<Nothing>(position, null)
}