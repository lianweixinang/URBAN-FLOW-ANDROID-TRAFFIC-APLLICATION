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
import ntutifm.game.google.net.ApiClass.Parking
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

    object FeedEntry1 : BaseColumns {
        const val TABLE_NAME = "Fav_Route"
        const val COLUMN_NAME_RoadId = "RoadId"
        const val COLUMN_NAME_RoadName = "RoadName"
        const val COLUMN_NAME_LinkId = "LinkId"
        const val COLUMN_NAME_time = "time"
    }

    object FeedEntry2 : BaseColumns {
        const val TABLE_NAME = "Fav_Gas"
        const val COLUMN_NAME_longitude = "longitude"
        const val COLUMN_NAME_latitude = "latitude"
        const val COLUMN_NAME_Gas = "Gas_name"
        const val COLUMN_NAME_time = "time"
    }

    object FeedEntry3 : BaseColumns {
        const val TABLE_NAME = "Fav_CCTV"
        const val COLUMN_NAME_url = "url"
        const val COLUMN_NAME_time = "time"
    }

    object FeedEntry4 : BaseColumns {
        const val TABLE_NAME = "Fav_parking"
        const val COLUMN_NAME_longitude = "longitude"
        const val COLUMN_NAME_latitude = "latitude"
        const val COLUMN_NAME_Parking = "P_name"
        const val COLUMN_NAME_time = "time"
    }
}

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_RoadId} TEXT," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_RoadName} TEXT," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_time} TEXT);"

private const val SQL_CREATE_ENTRIES1 =
    "CREATE TABLE ${FeedReaderContract.FeedEntry1.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry1.COLUMN_NAME_RoadId} TEXT," +
            "${FeedReaderContract.FeedEntry1.COLUMN_NAME_RoadName} TEXT," +
            "${FeedReaderContract.FeedEntry1.COLUMN_NAME_LinkId} TEXT," +
            "${FeedReaderContract.FeedEntry1.COLUMN_NAME_time} TEXT);"

private const val SQL_CREATE_ENTRIES2 =
    "CREATE TABLE ${FeedReaderContract.FeedEntry2.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry2.COLUMN_NAME_latitude} TEXT," +
            "${FeedReaderContract.FeedEntry2.COLUMN_NAME_longitude} TEXT," +
            "${FeedReaderContract.FeedEntry2.COLUMN_NAME_Gas} TEXT,"+
            "${FeedReaderContract.FeedEntry2.COLUMN_NAME_time}TEXT);"

private const val SQL_CREATE_ENTRIES3 =
    "CREATE TABLE ${FeedReaderContract.FeedEntry3.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry3.COLUMN_NAME_url} TEXT," +
            "${FeedReaderContract.FeedEntry3.COLUMN_NAME_time} TEXT);"

private const val SQL_CREATE_ENTRIES4 =
    "CREATE TABLE ${FeedReaderContract.FeedEntry4.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry4.COLUMN_NAME_latitude} TEXT," +
            "${FeedReaderContract.FeedEntry4.COLUMN_NAME_longitude} TEXT," +
            "${FeedReaderContract.FeedEntry4.COLUMN_NAME_Parking} TEXT,"+
            "${FeedReaderContract.FeedEntry4.COLUMN_NAME_time} TEXT);"


private const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"

private const val SQL_DELETE_ENTRIES1 =
    "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry1.TABLE_NAME}"

private const val SQL_DELETE_ENTRIES2 =
    "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry2.TABLE_NAME}"

private const val SQL_DELETE_ENTRIES3 =
    "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry3.TABLE_NAME}"

private const val SQL_DELETE_ENTRIES4 =
    "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry4.TABLE_NAME}"


class FeedReaderDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
    db.execSQL(SQL_CREATE_ENTRIES)
    db.execSQL(SQL_CREATE_ENTRIES1)
    db.execSQL(SQL_CREATE_ENTRIES2)
    db.execSQL(SQL_CREATE_ENTRIES3)
    db.execSQL(SQL_CREATE_ENTRIES4)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES1)
        db.execSQL(SQL_CREATE_ENTRIES2)
        db.execSQL(SQL_CREATE_ENTRIES3)
        db.execSQL(SQL_CREATE_ENTRIES4)
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

@RequiresApi(Build.VERSION_CODES.O)
fun dbAddFavRoad(Road: CityRoad, linkID:String, context: Context){
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put(FeedReaderContract.FeedEntry1.COLUMN_NAME_RoadId, Road.roadId)
        put(FeedReaderContract.FeedEntry1.COLUMN_NAME_RoadName, Road.roadName)
        put(FeedReaderContract.FeedEntry1.COLUMN_NAME_LinkId,linkID)
        put(FeedReaderContract.FeedEntry1.COLUMN_NAME_time, LocalDate.now().toString())
    }
    val newRowId = db?.insert(FeedReaderContract.FeedEntry1.TABLE_NAME, null, values)
    db.close()
}

@RequiresApi(Build.VERSION_CODES.O)
fun dbAddFavGas(longitude: String, latitude:String,GasName:String, context: Context){
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put(FeedReaderContract.FeedEntry2.COLUMN_NAME_Gas,GasName)
        put(FeedReaderContract.FeedEntry2.COLUMN_NAME_latitude,latitude)
        put(FeedReaderContract.FeedEntry2.COLUMN_NAME_longitude,longitude)
        put(FeedReaderContract.FeedEntry2.COLUMN_NAME_time, LocalDate.now().toString())
    }
    val newRowId = db?.insert(FeedReaderContract.FeedEntry2.TABLE_NAME, null, values)
    db.close()
}
@RequiresApi(Build.VERSION_CODES.O)
fun dbAddFavCCTV(url:String, context: Context){
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put(FeedReaderContract.FeedEntry3.COLUMN_NAME_url,url)
        put(FeedReaderContract.FeedEntry3.COLUMN_NAME_time, LocalDate.now().toString())
    }
    val newRowId = db?.insert(FeedReaderContract.FeedEntry3.TABLE_NAME, null, values)
    db.close()
}
@RequiresApi(Build.VERSION_CODES.O)
fun dbAddFavParking(Park:Parking, context: Context){
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put(FeedReaderContract.FeedEntry4.COLUMN_NAME_Parking,Park.parkingName)
        put(FeedReaderContract.FeedEntry4.COLUMN_NAME_latitude,Park.lat)
        put(FeedReaderContract.FeedEntry4.COLUMN_NAME_longitude,Park.lng)
        put(FeedReaderContract.FeedEntry4.COLUMN_NAME_time, LocalDate.now().toString())
    }
    val newRowId = db?.insert(FeedReaderContract.FeedEntry4.TABLE_NAME, null, values)
    db.close()
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
