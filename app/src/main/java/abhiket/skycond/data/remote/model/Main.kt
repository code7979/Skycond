package abhiket.skycond.data.remote.model

import com.google.gson.annotations.SerializedName


data class Main(
    @SerializedName("temp") var temp: Double,
    @SerializedName("feels_like") var feelsLike: Double,
    @SerializedName("temp_min") var tempMin: Double,
    @SerializedName("temp_max") var tempMax: Double,
    @SerializedName("pressure") var pressure: Long,
    @SerializedName("humidity") var humidity: Long,
    @SerializedName("sea_level") var seaLevel: Long,
    @SerializedName("grnd_level") var groundLevel: Long
)