package abhiket.skycond.data.remote.model

import com.google.gson.annotations.SerializedName


data class Wind(
    @SerializedName("speed") var speed: Double,
    @SerializedName("deg") var deg: Long,
    @SerializedName("gust") var gust: Double
)