package ntutifm.game.urbanflow.dataBase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ntutifm.game.urbanflow.apiClass.OilStation
import ntutifm.game.urbanflow.global.Resource

class OilStationRepository(context: Context) {
    private val oilStationDao:OilStationDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getOilStationDao()
    }

    suspend fun deleteFavorite(data:String){
        oilStationDao.deleteFavorite(data)
    }
    suspend fun addFavorite(data:OilStation){
        oilStationDao.insertFavorite(data)
    }
    suspend fun isRoadFavorite(data: String):Boolean{
        return oilStationDao.isOilStationFavorite(data)
    }
    fun getAllOilStation(): Flow<Resource<List<OilStation>>> {
        return oilStationDao.getAllStation()
        .map { Resource.Success(it)}
            .catch {
                Resource.Error(it)
            }
    }
}