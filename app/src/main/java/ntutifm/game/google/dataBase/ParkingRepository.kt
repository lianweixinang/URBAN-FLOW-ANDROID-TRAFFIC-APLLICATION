package ntutifm.game.google.dataBase

import android.content.Context
import ntutifm.game.google.apiClass.Parking

class ParkingRepository(context: Context) {
    private val parkingDao:ParkingDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getParkingDao()
    }

    fun deleteFavorite(data:String){
        parkingDao.deleteFavorite(data)
    }
    fun addFavorite(data:Parking){
        parkingDao.insertFavorite(data)
    }
    fun isRoadFavorite(data: String):Boolean{
        return parkingDao.isParkingFavorite(data)
    }
}