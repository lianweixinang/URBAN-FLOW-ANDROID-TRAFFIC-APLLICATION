package ntutifm.game.google.dataBase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.global.Resource

class ParkingRepository(context: Context) {
    private val parkingDao: ParkingDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getParkingDao()
    }

    suspend fun deleteFavorite(data: String) {
        parkingDao.deleteFavorite(data)
    }

    suspend fun addFavorite(data: Parking) {
        parkingDao.insertFavorite(data)
    }

    suspend fun isRoadFavorite(data: String): Boolean {
        return parkingDao.isParkingFavorite(data)
    }

    fun getAllStation(): Flow<Resource<List<Parking>>> {
        return parkingDao.getAllParking()
        .map { Resource.Success(it)}
            .catch {
                Resource.Error(it)
            }
    }
}