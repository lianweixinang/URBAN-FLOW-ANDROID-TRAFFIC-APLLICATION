package ntutifm.game.google.apiClass

import com.google.gson.annotations.SerializedName

data class Incident(
    @SerializedName("type") val type: String,
    @SerializedName("solved") val solved: String,
    @SerializedName("edited_time") val editedTime: String,
    @SerializedName("raise_time") val raiseTime: String,
    @SerializedName("auth") val auth: String,
    @SerializedName("part") val part: String,
    @SerializedName("title") val title: String,
)