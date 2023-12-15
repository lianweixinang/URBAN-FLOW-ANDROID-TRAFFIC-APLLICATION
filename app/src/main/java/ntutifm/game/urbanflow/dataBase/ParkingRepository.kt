package ntutifm.game.urbanflow.dataBase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ntutifm.game.urbanflow.apiClass.Parking
import ntutifm.game.urbanflow.global.Resource

class ParkingRepository(context: Context) {
    private val parkingDao: ParkingDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getParkingDao()
    }

    suspend fun insertParking(data: List<Parking>) {
        parkingDao.insertParking(data)
    }

    suspend fun deleteFavorite(data: String) {
        parkingDao.deleteFavorite(data)
    }

    suspend fun addFavorite(data: String) {
        parkingDao.insertFavorite(data)
    }

    suspend fun isRoadFavorite(data: String): Boolean {
        return parkingDao.isParkingFavorite(data)
    }

    fun getAllParking(): Flow<Resource<List<Parking>>> {
        return parkingDao.getAllParking()
        .map { Resource.Success(it)}
            .catch {
                Resource.Error(it)
            }
    }
    fun getAllFavorite(): Flow<Resource<List<Parking>>> {
        return parkingDao.getAllFavorite()
            .map { Resource.Success(it)}
            .catch {
                Resource.Error(it)
            }
    }
}