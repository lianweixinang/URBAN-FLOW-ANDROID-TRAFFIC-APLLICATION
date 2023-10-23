package ntutifm.game.google.dataBase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ntutifm.game.google.apiClass.RoadFavorite
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
    fun getAllRoad():Flow<Resource<List<RoadFavorite>>>{
        return roadFavoriteDao.getAllRoadFavorite()
            .map { Resource.Success(it)}
            .catch {
                Resource.Error(it)
            }
    }

}