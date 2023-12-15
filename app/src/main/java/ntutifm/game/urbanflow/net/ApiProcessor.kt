package ntutifm.game.urbanflow.net

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import ntutifm.game.urbanflow.apiClass.Camera
import ntutifm.game.urbanflow.apiClass.OilStation
import ntutifm.game.urbanflow.apiClass.Parking
import ntutifm.game.urbanflow.entity.sync.SyncIncident
import ntutifm.game.urbanflow.entity.sync.SyncOil
import ntutifm.game.urbanflow.entity.sync.SyncPosition
import ntutifm.game.urbanflow.entity.sync.SyncRoad
import ntutifm.game.urbanflow.entity.sync.SyncSpeed
import ntutifm.game.urbanflow.entity.sync.SyncWeather
import ntutifm.game.urbanflow.global.MyLog

class ApiProcessor {
    companion object {
        const val getCityRoadId = "getCityRoadId"
        const val getCityRoadSpeed = "getCityRoadSpeed"
        const val getIncident = "getIncident"
        const val getOil = "getOil"
        const val getWeather = "getWeather"
        const val getWeatherLocation = "getWeatherLocation"
        const val getFindCamera = "getFindCamera"
    }

    suspend fun getParking():APIResult<out Parking> {
        val myAPIService = RetrofitManager.api
        val response =
            myAPIService?.parkingList?.execute()
        try {
            if (response?.isSuccessful == true) {
                return APIResult.Success(response.body()!!)
            }
        } catch (exception: Exception) {
            return APIResult.Error(exception)
        }
        return APIResult.Error(null)
    }

    fun getCityRoadId(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.api
        val response =
            myAPIService?.getRoadId(successData[1])?.execute()
        try {
            if (response?.isSuccessful == true) {
                MyLog.d("SearchStart")
                SyncRoad.updateSearch(response.body()!!)
            } else {
                Log.d("RoadName", "Null")
            }
        } catch (exception: Exception) {
            Log.d("RoadName", exception.toString())
        }
    }

    fun getCityRoadSpeed(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.api
        val response =
            myAPIService?.getRoadSpeed(successData[1])?.execute()
        try {
            if (response?.isSuccessful == true) {
                MyLog.d("enterApi")
                MyLog.d(response?.body()!!.toString())
                SyncSpeed.updateSpeed(response?.body()!!)
            } else {
                Log.d("RoadSpeed", "Null")
            }
        } catch (exception: Exception) {
            Log.d("RoadSpeed", exception.toString())
        }
    }

    fun getIncident(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.api
        val response =
            myAPIService?.incidentList?.execute()
        try {
            if (response?.isSuccessful == true) {
                MyLog.d(response?.body()!!.toString())
                SyncIncident.updateIncident(response?.body()!!)
            } else {
                Log.d("parkingName", "Null")
            }
        } catch (exception: Exception) {
            Log.d("Title", exception.toString())
        }
    }

    fun getOil(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.api
        val response =
            myAPIService?.oilList?.execute()
        try {
            if (response?.isSuccessful == true) {
                MyLog.d(response?.body()!!.toString())
                SyncOil.updateOil(response?.body()!!)
            } else {
                MyLog.d("getOil")
            }
        } catch (exception: Exception) {
            Log.d("Title", exception.toString())
        }
    }

    fun getWeather(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.api
        val response =
            myAPIService?.weatherList?.execute()
        try {
            if (response?.isSuccessful == true) {
                MyLog.d("startWeatherApi")
                MyLog.d(response?.body()!!.toString())
                SyncWeather.updateWeather(response?.body()!!)
            } else {
                MyLog.d("getWeatherZero")
            }
        } catch (exception: Exception) {
            Log.d("Title", exception.toString())
        }
    }

    fun getWeatherLocation(
        c: Context,
        successData: ArrayList<String>,
        errorData: ArrayList<String>,
    ) {
        val myAPIService = RetrofitManager.api
        val response =
            myAPIService?.getWeatherLocationLatLng(successData[1], successData[2])?.execute()
        try {
            if (response?.isSuccessful == true) {
                SyncPosition.updateWeatherLocation(response.body()!!)
            } else {
                Log.d("district", "Null")
            }
        } catch (exception: Exception) {
            Log.d("DistrictTitle", exception.toString())
        }
    }

    suspend fun getCameraMark():APIResult<out Camera> {
        val myAPIService = RetrofitManager.api
        val response = myAPIService?.cameraMark?.execute()
        try {
            if (response?.isSuccessful == true) {
                return APIResult.Success(response.body()!!)
            }
        } catch (exception: Exception) {
            return APIResult.Error(exception)
        }
        return APIResult.Error(null)
    }

    suspend fun getFindCamera(latLng: LatLng) :APIResult<out Camera> {
        val myAPIService = RetrofitManager.api
        val response = myAPIService?.getFindCamera(latLng.latitude.toString(), latLng.longitude.toString())?.execute()
        try {
            if (response?.isSuccessful == true) {
                return APIResult.Success(listOf(response.body()!!))
            }
        } catch (exception: Exception) {
            return APIResult.Error(exception)
        }
        return APIResult.Error(null)
    }

    suspend fun getOilStation(): APIResult<out OilStation> {
        val myAPIService = RetrofitManager.api
        val response = myAPIService?.oilStationList?.execute()
        try {
            if (response?.isSuccessful == true) {
                return APIResult.Success(response.body()!!)
            }
        } catch (exception: Exception) {
            return APIResult.Error(exception)
        }
        return APIResult.Error(null)
    }
}

sealed class APIResult<T>{
    data class Success<T>(val data:List<T>): APIResult<T>()
    class Error(val exception: Throwable?) : APIResult<Nothing>()
}
