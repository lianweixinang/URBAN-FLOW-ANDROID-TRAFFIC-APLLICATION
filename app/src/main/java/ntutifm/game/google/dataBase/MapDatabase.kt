package ntutifm.game.google.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ntutifm.game.google.apiClass.SearchHistory

@Database(entities = [SearchHistory::class], version = 1, exportSchema = false)
abstract class MapDatabase: RoomDatabase() {
    abstract fun getDao(): SearchHistoryDao
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