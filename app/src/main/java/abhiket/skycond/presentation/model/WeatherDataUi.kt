package abhiket.skycond.presentation.model

import androidx.annotation.DrawableRes

data class WeatherDataUi(
    val          id:          Long,
    val          name:        String,
    val          lon:         Double,
    val          lat:         Double,
    val          date:        String,
    val          description: String,
    @DrawableRes
    val          icon:        Int,
    val          temp:        String,
    val          feelsLike:   String,
    val          tempMin:     String,
    val          tempMax:     String,
    val          pressure:    String,
    val          humidity:    String,
    val          visibility:  String,
    val          winSpeed:    String,
    val          windDeg:     String,
    val          clouds:      String
) {
    override fun toString(): String {
       return buildString {
           append('\n')
            append("Id: "); append(id); append('\n')
            append("Name:"); append(name); append('\n')
            append("Latitude:"); append(lat); append('\n')
            append("Longitude:"); append(lon); append('\n')
        }
    }
}