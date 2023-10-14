package ntutifm.game.google.dataBase

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.apiClass.RoadFavorite
import ntutifm.game.google.apiClass.SearchHistory
import ntutifm.game.google.global.Resource

class RoadFavoriteRepository(context: Context) {
    private val roadFavoriteDao : RoadFavoriteDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getRoadFavoriteDao()
    }
    suspend fun deleteFavorite(data:String){
        roadFavoriteDao.deleteFavorite(data)
    }
    suspend fun addFavorite(data:RoadFavorite){
        roadFavoriteDao.insertFavorite(data)
    }
    suspend fun isRoadFavorite(data: String):Boolean{
        return roadFavoriteDao.isRoadFavorite(data)
    }
    suspend fun getAllRoad():Flow<Resource<List<RoadFavorite>>>{
        return flow {
            try {
                emit(Resource.Success(roadFavoriteDao.getAllRoadFavorite()))
            } catch (ex: Exception) {
                emit(Resource.Error(ex))
            }
        }
    }

}