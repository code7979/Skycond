package abhiket.skycond.uitls

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertLongToDate(dateInMillis: Long): String {
    return SimpleDateFormat("EEE, h:mm a", Locale.getDefault()).format(Date(dateInMillis))
}