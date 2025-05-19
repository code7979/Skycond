package abhiket.skycond

import junit.framework.TestCase.assertEquals
import okhttp3.internal.Util.trimSubstring
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun trimSubstringPassed() {
        val string = " Patna "
        val trimmedSubstring = trimSubstring(string, 0, string.length)
        assertEquals(trimmedSubstring, "Patna")
    }

    @Test
    fun trimSubstringFailed() {
        val string = "Patna"
        val trimmedSubstring = trimSubstring(string, 0, string.length)
        assertEquals(trimmedSubstring, "Patna")
    }
}