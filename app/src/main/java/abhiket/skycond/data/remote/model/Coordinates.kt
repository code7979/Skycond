package abhiket.skycond.data.remote.model

import com.google.gson.annotations.SerializedName


data class Coordinates (
  @SerializedName("lon" ) var lon : Double,
  @SerializedName("lat" ) var lat : Double
)