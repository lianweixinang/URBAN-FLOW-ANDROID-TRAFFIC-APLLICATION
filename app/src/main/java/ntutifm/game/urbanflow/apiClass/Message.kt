package ntutifm.game.urbanflow.apiClass

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id") val id: String,
    @SerializedName("city") val city: String,
    @SerializedName("road") val road: String,
    @SerializedName("author") val author: String,
    @SerializedName("message") val message: String,
    @SerializedName("time") val time: String
)
