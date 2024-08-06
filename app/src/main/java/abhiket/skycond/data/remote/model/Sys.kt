package abhiket.skycond.data.remote.model

import com.google.gson.annotations.SerializedName


data class Sys (
  @SerializedName("type"    ) var type    : Int?    = null,
  @SerializedName("id"      ) var id      : Int?    = null,
  @SerializedName("country" ) var country : String,
  @SerializedName("sunrise" ) var sunrise : Long,
  @SerializedName("sunset"  ) var sunset  : Long
)