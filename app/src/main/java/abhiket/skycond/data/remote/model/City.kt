package abhiket.skycond.data.remote.model

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("name") val name: String,
    @SerializedName("local_names") val localNames: Any? = null,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("country") val country: String,
    @SerializedName("state") val state: String
)