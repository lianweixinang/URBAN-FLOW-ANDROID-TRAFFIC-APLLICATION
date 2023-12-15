package ntutifm.game.urbanflow.apiClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Parking(
    @PrimaryKey(autoGenerate = true)
    val id :Int?,
    @SerializedName("CarParkName") val parkingName:String,
    @SerializedName("PositionLat") val latitude: Double,
    @SerializedName("PositionLon") val longitude: Double,
    val isFavorite: Boolean = false
)