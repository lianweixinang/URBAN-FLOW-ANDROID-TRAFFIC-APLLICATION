package ntutifm.game.google.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.apiClass.CitySpeed
import ntutifm.game.google.apiClass.OilStation
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.apiClass.RoadFavorite
import ntutifm.game.google.apiClass.SearchHistory
@Database(entities = [SearchHistory::class, RoadFavorite::class,Camera::class,Parking::class,OilStation::class], version = 1, exportSchema = false)
abstract class MapDatabase: RoomDatabase() {
    abstract fun getSearchHistoryDao(): SearchHistoryDao
    abstract fun getRoadFavoriteDao(): RoadFavoriteDao
    abstract fun getCameraDao():CameraDao
    abstract fun getParkingDao():ParkingDao
    abstract fun getOilStationDao():OilStationDao
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