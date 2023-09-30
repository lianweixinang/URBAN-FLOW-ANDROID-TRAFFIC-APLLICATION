package ntutifm.game.google.apiClass

import com.google.gson.annotations.SerializedName

data class CitySpeed (
    @SerializedName("Direction") val direction : String,
    @SerializedName("AvgSpeed") val avgSpeed :Double,
    @SerializedName("Volume") val volume :Int = 99
)