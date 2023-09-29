package ntutifm.game.google.net.apiClass

import com.google.gson.annotations.SerializedName

data class Camera(
    @SerializedName("ID") val id: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Road") val road: String?,
    @SerializedName("Introduction") val introduction: String,
    @SerializedName("Session") val session: String,
    @SerializedName("Direction") val direction: String,
    @SerializedName("Limit") val limit: String,
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Longitude") val longitude: Double,
    @SerializedName("Distance") val distance: Int
)