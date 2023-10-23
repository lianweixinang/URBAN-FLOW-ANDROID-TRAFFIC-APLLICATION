package ntutifm.game.google.dataBase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ntutifm.game.google.apiClass.CCTV
import ntutifm.game.google.global.Resource

class CCTVRepository(context: Context) {
    private val cctvDao: CCTVDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getCCTVDao()
    }

    suspend fun deleteFavorite(data: String) {
        cctvDao.deleteFavorite(data)
    }

    suspend fun addFavorite(data: CCTV) {
        cctvDao.insertFavorite(data)
    }

    suspend fun isCCTVFavorite(data: String): Boolean {
        return cctvDao.isCCTVFavorite(data)
    }

    fun getAllCCTV(): Flow<Resource<List<CCTV>>> {
        return cctvDao.getAllCCTV()
            .map { Resource.Success(it)}
            .catch {
                Resource.Error(it)
            }
    }
}