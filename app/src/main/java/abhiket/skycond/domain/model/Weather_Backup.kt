package abhiket.skycond.domain.model

const val fd = 3

data class Weatherfd(
    val id:          Long,
    val name:        String,
    val country:     String,
    val latitude:    Double,
    val longitude:   Double,

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
    val visibility:  Long,
    val winSpeed:    Double,
    val windDeg:     Long,
    val clouds:      Long,
    val sunrise:     Long,
    val sunset:      Long,
    val timeZone:    Long,
    val lastUpdate:  Long, //IN SECOND
) {

}
