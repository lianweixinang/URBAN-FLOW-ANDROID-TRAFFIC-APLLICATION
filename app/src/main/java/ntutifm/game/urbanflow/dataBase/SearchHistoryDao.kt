package ntutifm.game.urbanflow.dataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ntutifm.game.urbanflow.apiClass.SearchHistory

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(vararg searchHistory:SearchHistory)
    @Update
    fun updateHistory(vararg searchHistory:SearchHistory):Int
    @Query("DELETE FROM SearchHistory WHERE roadName = :roadName")
    fun deleteHistory(roadName: String)
    @Query("DELETE FROM SearchHistory")
    fun deleteAllHistory()
    @Query("SELECT * FROM SearchHistory ORDER BY SearchTime DESC")
    fun getAllHistory():LiveData<List<SearchHistory>>
}