package ntutifm.game.urbanflow.apiClass

import com.google.gson.annotations.SerializedName

data class Oil (
    @SerializedName("date") val date: String,
    @SerializedName("92") val nineTwo: String,
    @SerializedName("95") val nineFive: String,
    @SerializedName("98") val nineEight: String,
    @SerializedName("super") val superOil: String
)