package ntutifm.game.google.apiClass

import com.google.gson.annotations.SerializedName

data class OilStation (
    @SerializedName("Station") val station: String,
    @SerializedName("Longitude") val logitude: Double,
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Address") val address: String
)