package ntutifm.game.google.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ntutifm.game.google.apiClass.OilStation

@Dao
interface OilStationDao{

    @Insert
    fun insertFavorite(vararg oilStation: OilStation)
    @Query("DELETE FROM OilStation WHERE station = :station")
    fun deleteFavorite(station: String)
    @Query("SELECT EXISTS (SELECT 1 FROM OilStation WHERE station= :station LIMIT 1)")
    fun isOilStationFavorite(station: String): Boolean
}