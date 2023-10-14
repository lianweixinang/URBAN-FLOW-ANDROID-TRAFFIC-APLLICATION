package ntutifm.game.google.dataBase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ntutifm.game.google.apiClass.OilStation
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.global.Resource

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
    suspend fun getAllOilStation(): Flow<Resource<List<OilStation>>> {
        return flow {
            try {
                emit(Resource.Success(oilStationDao.getAllStation()))
            } catch (ex: Exception) {
                emit(Resource.Error(ex))
            }
        }
    }
}