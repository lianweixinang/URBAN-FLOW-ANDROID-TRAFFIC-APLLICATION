package ntutifm.game.google.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ntutifm.game.google.apiClass.CCTV

@Dao
interface CCTVDao{

    @Insert
     suspend fun insertFavorite(vararg cctv: CCTV)
    @Query("DELETE FROM CCTV WHERE name  = :name")
    suspend fun deleteFavorite(name: String)
    @Query("SELECT EXISTS (SELECT 1 FROM CCTV WHERE name= :name LIMIT 1)")
    suspend fun isCCTVFavorite(name: String): Boolean
    @Query("SELECT * FROM CCTV")
    suspend fun getAllcctv() : List<CCTV>
}