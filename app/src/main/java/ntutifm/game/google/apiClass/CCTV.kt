package ntutifm.game.google.apiClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class CCTV(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID") val id: Int?,
    @SerializedName("Name") val name:String,
    @SerializedName("URL") val url:String)