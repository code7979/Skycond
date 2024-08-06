package abhiket.skycond.uitls

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertLongToDate(dateInMillis: Long): String {
    return SimpleDateFormat("EEEE | MMM d", Locale.getDefault()).format(Date(dateInMillis))
}