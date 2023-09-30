package ntutifm.game.google.dataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ntutifm.game.google.apiClass.SearchHistory
import ntutifm.game.google.entity.Road

@Dao
interface RoadFavoriteDao {
    @Insert
    fun insertRoad(vararg road: Road)
    @Update
    fun updateRoad(vararg road: Road):Int
    @Delete
    fun deleteRoad(vararg road: Road)
    @Query("DELETE FROM SearchHistory")
    fun deleteAllRoad()
    @Query("SELECT * FROM SearchHistory ORDER BY SearchTime DESC")
    fun getAllRoad():LiveData<List<SearchHistory>>
}