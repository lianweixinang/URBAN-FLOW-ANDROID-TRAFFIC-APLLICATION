package ntutifm.game.urbanflow.apiClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class OilStation (
    @PrimaryKey(autoGenerate = true)
    val id :Int?,
    @SerializedName("Station") val station: String,
    @SerializedName("Longitude") val longitude: Double,
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Address") val address: String,
)