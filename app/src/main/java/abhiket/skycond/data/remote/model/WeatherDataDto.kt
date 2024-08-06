package abhiket.skycond.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeatherDataDto(
    @SerializedName( "coord")      var coordinates: Coordinates,
    @SerializedName( "weather")    var weather:     ArrayList<Weather>,
    @SerializedName( "base")       var base:        String,
    @SerializedName( "main")       var main:        Main,
    @SerializedName( "visibility") var visibility:  Long,
    @SerializedName( "wind")       var wind:        Wind,
    @SerializedName( "rain")       var rain:        Rain,
    @SerializedName( "clouds")     var clouds:      Clouds,
    @SerializedName( "dt")         var date:        Long,
    @SerializedName( "sys")        var sys:         Sys,
    @SerializedName( "timezone")   var timezone:    Long,
    @SerializedName( "id")         var id:          Long,
    @SerializedName( "name")       var name:        String,
    @SerializedName( "cod")        var cod:         Int
)