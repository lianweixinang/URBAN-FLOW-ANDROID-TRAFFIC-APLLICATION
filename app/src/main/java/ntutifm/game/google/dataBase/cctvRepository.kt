package ntutifm.game.google.dataBase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ntutifm.game.google.apiClass.CCTV
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.global.Resource

class cctvRepository(context: Context) {
    private val cctvDao : cctvDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getCCTVDao()
    }

    suspend fun deleteFavorite(data:String){
        cctvDao.deleteFavorite(data)
    }
    suspend fun addFavorite(data: CCTV){
        cctvDao.insertFavorite(data)
    }
    suspend fun isCCTVFavorite(data: String):Boolean{
        return cctvDao.isCCTVFavorite(data)
    }
    suspend fun getAllCCTV(): Flow<Resource<List<CCTV>>> {
        return flow {
            try {
                emit(Resource.Success(cctvDao.getAllcctv()))
            } catch (ex: Exception) {
                emit(Resource.Error(ex))
            }
        }
    }
}