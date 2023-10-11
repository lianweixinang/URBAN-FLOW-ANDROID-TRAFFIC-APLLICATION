package ntutifm.game.google.dataBase

import android.content.Context
import androidx.lifecycle.LiveData
import ntutifm.game.google.apiClass.RoadFavorite
import ntutifm.game.google.apiClass.SearchHistory

class RoadFavoriteRepository(context: Context) {
    private val roadFavoriteDao : RoadFavoriteDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getRoadFavoriteDao()
    }

    fun deleteFavorite(data:String){
        roadFavoriteDao.deleteFavorite(data)
    }
    fun addFavorite(data:RoadFavorite){
        roadFavoriteDao.insertFavorite(data)
    }
    fun isRoadFavorite(data: String):Boolean{
        return roadFavoriteDao.isRoadFavorite(data)
    }
}