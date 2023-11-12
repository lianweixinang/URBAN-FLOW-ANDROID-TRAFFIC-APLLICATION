package ntutifm.game.urbanflow.apiClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoadFavorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo("RoadId")
    val roadId: String,
    @ColumnInfo("RoadName")
    val roadName: String,
)