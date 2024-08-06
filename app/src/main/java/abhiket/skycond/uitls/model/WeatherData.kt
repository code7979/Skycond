package abhiket.skycond.uitls.model

data class WeatherData(
    val id:          Long,
    val name:        String,
    val country:     String,
    val lon:         Double,
    val lat:         Double,
    val lastUpdate:  Long, //IN SECOND
    val main:        String,
    val icon:        String,
    var temp:        Double,
    var feelsLike:   Double,
    var tempMin:     Double,
    var tempMax:     Double,
    var pressure:    Long,
    var humidity:    Long,
    var seaLevel:    Long,
    var groundLevel: Long,
    val visibility:  String,
    val winSpeed:    String,
    val windDeg:     String,
    val clouds:      String,
    val sunrise:     Long,
    val sunset:      Long,
    val timeZone:    Long
)
