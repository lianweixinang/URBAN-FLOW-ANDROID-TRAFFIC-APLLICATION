package ntutifm.game.google.apiClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @SerializedName("RoadId")
    @ColumnInfo("RoadId")
    val roadId: String,
    @SerializedName("RoadName")
    @ColumnInfo("RoadName")
    val roadName: String,
    @ColumnInfo("SearchTime")
    var searchTime: Long?,
)