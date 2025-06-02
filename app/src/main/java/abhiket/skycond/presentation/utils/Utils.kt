package abhiket.skycond.presentation.utils

import abhiket.skycond.R
import androidx.annotation.StringRes
import retrofit2.HttpException
import java.io.IOException

@Suppress("NOTHING_TO_INLINE")
@StringRes
internal inline fun Int.toStringRes(): Int {
    return when (this) {
        404 -> R.string.error_http_404
        403 -> R.string.error_http_403
        500 -> R.string.error_http_500
        502 -> R.string.error_http_502
        503 -> R.string.error_http_503
        else -> R.string.error_fetching_data
    }
}

fun Throwable.asStringValue(): StringValue {
    return when (this) {
        is HttpException -> StringValue.StringResource(code().toStringRes())
        is IOException -> StringValue.StringResource(R.string.error_network_issue)
        is KotlinNullPointerException -> StringValue.StringResource(R.string.error_fetching_data)
        else -> StringValue.StringResource(R.string.error_fetch_data)
    }
}

inline fun <T> MutableList<T>.onEachRemove(action: (T) -> Unit) {
    if (isNotEmpty()) {
        while (0 < size) action(removeAt(0))
    }
}