package ntutifm.game.google.dataBase

import android.content.Context
import ntutifm.game.google.apiClass.OilStation
import ntutifm.game.google.apiClass.Parking

class OilStationRepository(context: Context) {
    private val oilStationDao:OilStationDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getOilStationDao()
    }

    fun deleteFavorite(data:String){
        oilStationDao.deleteFavorite(data)
    }
    fun addFavorite(data:OilStation){
        oilStationDao.insertFavorite(data)
    }
    fun isRoadFavorite(data: String):Boolean{
        return oilStationDao.isOilStationFavorite(data)
    }
}