package abhiket.skycond.data.remote.model

import com.google.gson.annotations.SerializedName


data class Rain(
    @SerializedName("1h") var inHour: Double? = null
)