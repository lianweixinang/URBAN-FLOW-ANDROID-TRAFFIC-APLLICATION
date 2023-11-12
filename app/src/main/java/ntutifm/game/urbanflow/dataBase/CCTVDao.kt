package ntutifm.game.urbanflow.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ntutifm.game.urbanflow.apiClass.CCTV

@Dao
interface CCTVDao{

    @Insert
     suspend fun insertFavorite(vararg cctv: CCTV)
    @Query("DELETE FROM CCTV WHERE name  = :name")
    suspend fun deleteFavorite(name: String)
    @Query("SELECT EXISTS (SELECT 1 FROM CCTV WHERE name= :name LIMIT 1)")
    suspend fun isCCTVFavorite(name: String): Boolean
    @Query("SELECT * FROM CCTV")
    fun getAllCCTV() : Flow<List<CCTV>>
}