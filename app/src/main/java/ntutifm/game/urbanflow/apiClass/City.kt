package ntutifm.game.urbanflow.apiClass

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("CityID") val cityID: String,
    @SerializedName("CityName") val cityName: String,
    @SerializedName("CityCode") val cityCode: String,
    @SerializedName("City") val city: String,
    @SerializedName("CountryID") val countryID: String,
    @SerializedName("Version") val version: String,
)