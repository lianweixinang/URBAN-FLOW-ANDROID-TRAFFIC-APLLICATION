package ntutifm.game.google.entity


import android.annotation.SuppressLint
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
import ntutifm.game.google.net.apiClass.CityRoad
import ntutifm.game.google.net.apiClass.OilStation
import ntutifm.game.google.net.apiClass.Parking
import java.time.LocalDate


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
        const val COLUMN_NAME_RoadId = "ID"
        const val COLUMN_NAME_RoadName = "NAME"
        const val COLUMN_NAME_time = "time"
    }

    object FeedEntry2 : BaseColumns {
        const val TABLE_NAME = "Fav_Gas"
        const val COLUMN_NAME_longitude = "longitude"
        const val COLUMN_NAME_latitude = "latitude"
        const val COLUMN_NAME_Gas = "NAME"
        const val COLUMN_NAME_time = "time"
    }

    object FeedEntry3 : BaseColumns {
        const val TABLE_NAME = "Fav_CCTV"
        const val COLUMN_NAME_url = "url"
        const val Column_Name_name = "NAME"
        const val COLUMN_NAME_time = "time"
    }

    object FeedEntry4 : BaseColumns {
        const val TABLE_NAME = "Fav_parking"
        const val COLUMN_NAME_longitude = "longitude"
        const val COLUMN_NAME_latitude = "latitude"
        const val COLUMN_NAME_Parking = "NAME"
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
            "${FeedReaderContract.FeedEntry1.COLUMN_NAME_time} TEXT);"

private const val SQL_CREATE_ENTRIES2 =
    "CREATE TABLE ${FeedReaderContract.FeedEntry2.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry2.COLUMN_NAME_latitude} TEXT," +
            "${FeedReaderContract.FeedEntry2.COLUMN_NAME_longitude} TEXT," +
            "${FeedReaderContract.FeedEntry2.COLUMN_NAME_Gas} TEXT," +
            "${FeedReaderContract.FeedEntry2.COLUMN_NAME_time}TEXT);"

private const val SQL_CREATE_ENTRIES3 =
    "CREATE TABLE ${FeedReaderContract.FeedEntry3.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry3.Column_Name_name} TEXT," +
            "${FeedReaderContract.FeedEntry3.COLUMN_NAME_url} TEXT," +
            "${FeedReaderContract.FeedEntry3.COLUMN_NAME_time} TEXT);"

private const val SQL_CREATE_ENTRIES4 =
    "CREATE TABLE ${FeedReaderContract.FeedEntry4.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry4.COLUMN_NAME_latitude} TEXT," +
            "${FeedReaderContract.FeedEntry4.COLUMN_NAME_longitude} TEXT," +
            "${FeedReaderContract.FeedEntry4.COLUMN_NAME_Parking} TEXT," +
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
        db.execSQL(SQL_DELETE_ENTRIES1)
        db.execSQL(SQL_DELETE_ENTRIES2)
        db.execSQL(SQL_DELETE_ENTRIES3)
        db.execSQL(SQL_DELETE_ENTRIES4)
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

@SuppressLint("Recycle")
fun dbReset(context: Context){
    var dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    db.execSQL(SQL_DELETE_ENTRIES)
    db.execSQL(SQL_DELETE_ENTRIES1)
    db.execSQL(SQL_DELETE_ENTRIES2)
    db.execSQL(SQL_DELETE_ENTRIES3)
    db.execSQL(SQL_DELETE_ENTRIES4)
}
@SuppressLint("Recycle")
fun dbDeleteHistory(search: String, context: Context) {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    db.execSQL("DELETE FROM history WHERE history.roadName = '$search'")
    db.close()
}


fun dbDisplayHistory(context: Context): List<CityRoad> {
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

open class FavoriteType(val name: String, val table:String)
open class Road(name: String, table:String= FeedReaderContract.FeedEntry1.TABLE_NAME) : FavoriteType(name,table)
open class Parking(name: String, table:String= FeedReaderContract.FeedEntry4.TABLE_NAME) : FavoriteType(name,table)
open class GasStation(name: String, table:String= FeedReaderContract.FeedEntry2.TABLE_NAME) : FavoriteType(name,table)
open class CCTV(name: String, table:String= FeedReaderContract.FeedEntry3.TABLE_NAME,val url:String) : FavoriteType(name,table)



@SuppressLint("Recycle")
fun dbFavDisplay(search: FavoriteType, context: Context):Boolean {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.readableDatabase
    val res: Cursor =
        db.rawQuery(
            "SELECT * FROM ${search.table} where Name ='${search.name}' ",
            null
        )
    return res.count > 0
}
fun dbFavCDelete(search: FavoriteType, context: Context) {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    db.execSQL("DELETE FROM ${search.table} WHERE Name = '${search.name}'")
    db.close()
}
@SuppressLint("Recycle")
@RequiresApi(Build.VERSION_CODES.O)
fun dbAddFavRoad(Road: CityRoad, context: Context) {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put(FeedReaderContract.FeedEntry1.COLUMN_NAME_RoadId, Road.roadId)
        put(FeedReaderContract.FeedEntry1.COLUMN_NAME_RoadName, Road.roadName)
        put(FeedReaderContract.FeedEntry1.COLUMN_NAME_time, LocalDate.now().toString())
    }
    val newRowId = db?.insert(FeedReaderContract.FeedEntry1.TABLE_NAME, null, values)
}

@SuppressLint("Recycle")
@RequiresApi(Build.VERSION_CODES.O)
fun dbAddFavGas(data:OilStation, context: Context) {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put(FeedReaderContract.FeedEntry2.COLUMN_NAME_Gas, data.station)
        put(FeedReaderContract.FeedEntry2.COLUMN_NAME_latitude, data.latitude)
        put(FeedReaderContract.FeedEntry2.COLUMN_NAME_longitude, data.logitude)
        put(FeedReaderContract.FeedEntry2.COLUMN_NAME_time, LocalDate.now().toString())
    }
    val newRowId = db?.insert(FeedReaderContract.FeedEntry2.TABLE_NAME, null, values)
}

@SuppressLint("Recycle")
@RequiresApi(Build.VERSION_CODES.O)
fun dbAddFavCCTV(cctv: CCTV, context: Context) {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put(FeedReaderContract.FeedEntry3.Column_Name_name,cctv.name )
        put(FeedReaderContract.FeedEntry3.COLUMN_NAME_url, cctv.url)
        put(FeedReaderContract.FeedEntry3.COLUMN_NAME_time, LocalDate.now().toString())
    }
    val newRowId = db?.insert(FeedReaderContract.FeedEntry3.TABLE_NAME, null, values)

}

@SuppressLint("Recycle")
@RequiresApi(Build.VERSION_CODES.O)
fun dbAddFavParking(park: Parking, context: Context) {
    val dbHelper = FeedReaderDbHelper(context)
    val db = dbHelper.writableDatabase
    val values = ContentValues().apply {
        put(FeedReaderContract.FeedEntry4.COLUMN_NAME_Parking, park.parkingName)
        put(FeedReaderContract.FeedEntry4.COLUMN_NAME_latitude, park.latitude)
        put(FeedReaderContract.FeedEntry4.COLUMN_NAME_longitude, park.longitude)
        put(FeedReaderContract.FeedEntry4.COLUMN_NAME_time, LocalDate.now().toString())
    }
    val newRowId = db?.insert(FeedReaderContract.FeedEntry4.TABLE_NAME, null, values)
}


@SuppressLint("Recycle")
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
        db.rawQuery(
            "SELECT * FROM history WHERE history.roadName = '${search.roadName}'",
            null
        )
    if (c.count > 0) {
        db.execSQL("DELETE FROM history WHERE history.roadName = '${search.roadName}'")
    }

    val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
}
