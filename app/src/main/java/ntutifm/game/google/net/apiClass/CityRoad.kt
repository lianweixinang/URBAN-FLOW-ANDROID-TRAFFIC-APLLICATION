package ntutifm.game.google.net.apiClass

import com.google.gson.annotations.SerializedName

data class CityRoad(
    @SerializedName("RoadId") var roadId:String,
    @SerializedName("RoadName") var roadName:String
)