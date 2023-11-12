package ntutifm.game.urbanflow.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ntutifm.game.urbanflow.apiClass.CCTV
import ntutifm.game.urbanflow.apiClass.Camera
import ntutifm.game.urbanflow.apiClass.OilStation
import ntutifm.game.urbanflow.apiClass.Parking
import ntutifm.game.urbanflow.apiClass.RoadFavorite
import ntutifm.game.urbanflow.apiClass.SearchHistory
@Database(entities = [SearchHistory::class, RoadFavorite::class,Camera::class,Parking::class,OilStation::class,CCTV::class], version = 1, exportSchema = false)
abstract class MapDatabase: RoomDatabase() {
    abstract fun getSearchHistoryDao(): SearchHistoryDao
    abstract fun getRoadFavoriteDao(): RoadFavoriteDao
    abstract fun getCameraDao():CameraDao
    abstract fun getParkingDao():ParkingDao
    abstract fun getOilStationDao():OilStationDao
    abstract fun getCCTVDao():CCTVDao
    companion object {
        private var instance: MapDatabase? = null
        fun getDatabase(context: Context): MapDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MapDatabase::class.java,
                    "map_database"
                ).build()
            }
            return instance!!
        }
    }
}