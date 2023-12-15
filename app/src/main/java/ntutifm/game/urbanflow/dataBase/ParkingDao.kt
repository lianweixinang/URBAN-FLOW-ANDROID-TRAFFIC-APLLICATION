package ntutifm.game.urbanflow.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ntutifm.game.urbanflow.apiClass.Parking

@Dao
interface ParkingDao{
    @Insert
    suspend fun insertParking(parkings: List<Parking>)
    @Query("UPDATE Parking SET isFavorite = 1 WHERE parkingName = :parkingName")
    suspend fun insertFavorite(parkingName: String)
    @Query("UPDATE Parking SET isFavorite = 0 WHERE parkingName = :parkingName")
    suspend fun deleteFavorite(parkingName: String)
    @Query("SELECT EXISTS (SELECT 1 FROM Parking WHERE parkingName = :parkingName AND isFavorite = 1 LIMIT 1)")
    suspend fun isParkingFavorite(parkingName: String): Boolean
    @Query("SELECT * FROM parking")
    fun getAllParking() : Flow<List<Parking>>

    @Query("SELECT * FROM Parking WHERE isFavorite = 1")
    fun getAllFavorite(): Flow<List<Parking>>

}