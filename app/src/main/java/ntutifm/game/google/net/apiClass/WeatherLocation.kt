package ntutifm.game.google.net.apiClass

import com.google.gson.annotations.SerializedName

data class WeatherLocation (
    @SerializedName("district") val districtName: String = "district"
)