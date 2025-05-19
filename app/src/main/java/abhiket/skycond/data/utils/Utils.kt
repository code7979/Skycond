package abhiket.skycond.data.utils

import retrofit2.HttpException
import java.io.IOException

private val REGEX_SPLIT_COMMA = "\\s*,\\s*".toRegex()

fun String.skipLeadingWhitespace(startIndex: Int = 0, endIndex: Int = length): Int {
    for (i in startIndex until endIndex) {
        when (this[i]) {
            '\t', '\n', '\u000c', '\r', ' ' -> Unit
            else -> return i
        }
    }
    return endIndex
}

fun String.skipTrailingWhitespace(startIndex: Int = 0, endIndex: Int = length): Int {
    for (i in endIndex - 1 downTo startIndex) {
        when (this[i]) {
            '\t', '\n', '\u000c', '\r', ' ' -> Unit
            else -> return i + 1
        }
    }
    return startIndex
}

fun String.trimSubstring(startIndex: Int = 0, endIndex: Int = length): String {
    val start = skipLeadingWhitespace(startIndex, endIndex)
    val end = skipTrailingWhitespace(startIndex, endIndex)
    return substring(start, end)
}

fun String.toLocationParts(limit: Int = 0): List<String> {
    return split(REGEX_SPLIT_COMMA, limit.coerceAtLeast(0))
}

inline fun <T> wrapWithResultForRemote(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: KotlinNullPointerException) {
        Result.failure(e)
    }
}
