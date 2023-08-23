package ntutifm.game.google.entity


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiClass.CityRoad
import java.time.LocalDate
import java.time.ZoneId


object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "history"
        const val COLUMN_NAME_RoadId = "RoadId"
        const val COLUMN_NAME_RoadName = "RoadName"
        const val COLUMN_NAME_time = "time"
    }
}

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_RoadId} TXT," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_RoadName} TXT," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_time} TEXT);"


private const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"


class FeedReaderDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Map.db"
    }
}

fun dbDeleteHistory(search: String, context: Context) {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    db.execSQL("DELETE FROM history WHERE history.roadName = '$search'")
    db.close()
}
fun dbDisplayHistory(context: Context):List<CityRoad> {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.readableDatabase
    val c: Cursor =
        db.rawQuery(
            "SELECT * FROM history ORDER BY history.time, history._id DESC",
            null
        )
    var history: MutableList<CityRoad> = mutableListOf()
    if (c.count != 0) {
        Log.d("Count", c.count.toString())
        with(c) {
            moveToFirst()
            while (!isAfterLast) {
                MyLog.d(getString(2))
                history.add(CityRoad(getString(1).orEmpty(), getString(2).orEmpty()))
                moveToNext()
            }
        }
    }
    c.close()
    db.close()
    return history
}
//
@RequiresApi(Build.VERSION_CODES.O)
fun dbAddHistory(search: CityRoad, context: Context) {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put(FeedReaderContract.FeedEntry.COLUMN_NAME_RoadId, search.roadId)
        put(FeedReaderContract.FeedEntry.COLUMN_NAME_RoadName, search.roadName)
        put(FeedReaderContract.FeedEntry.COLUMN_NAME_time, LocalDate.now().toString())
    }
    val c: Cursor =
        db.rawQuery("SELECT * FROM history WHERE history.roadName = '${search.roadName}'",
            null)
    if (c.count > 0) {
        db.execSQL("DELETE FROM history WHERE history.roadName = '${search.roadName}'")
    }

    val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
    c.close()
    db.close()
}
