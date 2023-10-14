package ntutifm.game.google.dataBase

import android.content.Context
import ntutifm.game.google.apiClass.CCTV

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
    suspend fun getAllcctv():List<CCTV>{
        return cctvDao.getAllcctv()
    }
}