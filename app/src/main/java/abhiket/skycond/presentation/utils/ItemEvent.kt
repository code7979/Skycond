package abhiket.skycond.presentation.utils

sealed class ItemEvent<out T>(val position: Int, val data: T) {
    class Add<out T>(position: Int, addData: T) : ItemEvent<T>(position, addData)
    class Remove<out T>(position: Int, removeData: T) : ItemEvent<T>(position, removeData)
    class Update<out T>(position: Int, updateData: T) : ItemEvent<T>(position, updateData)
}