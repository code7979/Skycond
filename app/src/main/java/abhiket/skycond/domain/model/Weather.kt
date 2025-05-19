package abhiket.skycond.domain.model

data class Weather(
    val conditionId: Long,
    val main:        String,
    val description: String,
    val icon:        String,
    var temp:        Double,
    var feelsLike:   Double,
    var tempMin:     Double,
    var tempMax:     Double,
    var pressure:    Long,
    var humidity:    Long,
    var seaLevel:    Long,
    var groundLevel: Long,
    val visibility:  Long,
    val windSpeed:    Double,
    val windDeg:     Long,
    val clouds:      Long,
    val sunrise:     Long,
    val sunset:      Long,
    val timeZone:    Long,
    val lastUpdate:  Long, //IN SECOND
)
