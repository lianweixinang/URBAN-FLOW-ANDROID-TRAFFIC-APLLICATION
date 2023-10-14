package ntutifm.game.google.dataBase

import android.content.Context
import ntutifm.game.google.apiClass.Parking

class ParkingRepository(context: Context) {
    private val parkingDao:ParkingDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getParkingDao()
    }

    suspend fun deleteFavorite(data:String){
        parkingDao.deleteFavorite(data)
    }
    suspend fun addFavorite(data:Parking){
        parkingDao.insertFavorite(data)
    }
    suspend fun isRoadFavorite(data: String):Boolean{
        return parkingDao.isParkingFavorite(data)
    }
    suspend fun getAllStation():List<Parking>{
        return parkingDao.getAllParking()
    }
}