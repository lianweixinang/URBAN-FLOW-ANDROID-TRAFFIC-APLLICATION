package ntutifm.game.google.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ntutifm.game.google.apiClass.Parking

@Dao
interface ParkingDao{

    @Insert
    suspend fun insertFavorite(vararg parking: Parking)
    @Query("DELETE FROM Parking WHERE parkingName = :parkingName")
    suspend fun deleteFavorite(parkingName: String)
    @Query("SELECT EXISTS (SELECT 1 FROM Parking WHERE parkingName= :parkingName LIMIT 1)")
    suspend fun isParkingFavorite(parkingName: String): Boolean
    @Query("SELECT * FROM parking")
    suspend fun getAllParking() : List<Parking>
}