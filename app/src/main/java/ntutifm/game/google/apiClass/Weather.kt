package ntutifm.game.google.apiClass

import com.google.gson.annotations.SerializedName

data class Weather (
    @SerializedName("locationName") val locationName: String = "locationName",
    @SerializedName("WeatherDescription") val weatherDescription: String = "WeatherDescription",
    //降雨機率 per12h
    @SerializedName("PoP6h1") val pop6h1: String = "PoP6h1",
    @SerializedName("PoP6h2") val pop6h2: String = "PoP6h2",
    @SerializedName("PoP6h3") val pop6h3: String = "PoP6h3",
    @SerializedName("PoP6h4") val pop6h4: String = "PoP6h4",
    @SerializedName("Wx1") val wx1: String = "Wx1",
    @SerializedName("Wx2") val wx2: String = "Wx2",
    @SerializedName("Wx3") val wx3: String = "Wx3",
    @SerializedName("Wx4") val wx4: String = "Wx4",
    @SerializedName("Wx5") val wx5: String = "Wx5",
    @SerializedName("Wx6") val wx6: String = "Wx6",
    @SerializedName("Wx7") val wx7: String = "Wx7",
    @SerializedName("Wx8") val wx8: String = "Wx8",
    //Temperature per 3hr
    @SerializedName("T1") val t1: String = "T1",
    @SerializedName("T2") val t2: String = "T2",
    @SerializedName("T3") val t3: String = "T3",
    @SerializedName("T4") val t4: String = "T4",
    @SerializedName("T5") val t5: String = "T5",
    @SerializedName("T6") val t6: String = "T6",
    @SerializedName("T7") val t7: String = "T7",
    @SerializedName("T8") val t8: String = "T8"
)