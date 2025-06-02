package abhiket.skycond.presentation.utils

//sealed interface ItemState<out T> {
//    data class Success<out T>(val data: T) : ItemState<T>
//    data class Failure<out T>(val data: T, val message: StringValue) : ItemState<T>
//    data class Loading<out T>(val data: T) : ItemState<T>
//}

abstract class ItemState<T>(protected var position: Int, data: T?) {
    protected open var data: T = data

    class Success<T>(position: Int, data: T) : ItemState<T>(position, data) {
        override fun getPosition(): Int {
            return super.position
        }

        override fun getData(): T {
            return super.data
        }
    }

    class Loading<T>(position: Int) : ItemState<T>(position, null) {
        override fun getPosition(): Int {
            return super.position
        }
    }

    class Failure<T>(position: Int, stringValue: StringValue?) :
        ItemState<T>(position, null) {
        val exception: Exception? = null

        override fun getPosition(): Int {
            return super.position
        }
    }
}