package abhiket.skycond.presentation.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class StringValue {

    class StringResource(@StringRes val resId: Int, vararg val args: Any) : StringValue()
    data class DynamicString(val value: String) : StringValue()
    data object Empty : StringValue()


    fun asString(context: Context): String {
        return when (this) {
            is Empty -> ""
            is DynamicString -> value
            is StringResource -> context.getString(resId, args)
        }
    }
}