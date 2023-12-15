package ntutifm.game.urbanflow.apiClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Camera(
    @PrimaryKey(autoGenerate = true)
    val id :Int?,
    @SerializedName("ID") val cameraId: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Road") val road: String?,
    @SerializedName("Introduction") val introduction: String?,
    @SerializedName("Session") val session: String,
    @SerializedName("Direction") val direction: String,
    @SerializedName("Limit") val limit: String,
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Longitude") val longitude: Double,
    @SerializedName("Distance") var distance: Int,
    val isFavorite: Boolean = false
)