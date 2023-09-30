package ntutifm.game.google.apiClass

import com.google.gson.annotations.SerializedName

data class Parking(
    @SerializedName("CarParkName") val parkingName: String,
    @SerializedName("PositionLat") val latitude: Double,
    @SerializedName("PositionLon") val longitude: Double
)