package ntutifm.game.google.net

import android.content.Context
import android.util.Log
import ntutifm.game.google.entity.sync.SyncCamera
import ntutifm.game.google.entity.sync.SyncIncident
import ntutifm.game.google.entity.sync.SyncOil
import ntutifm.game.google.entity.sync.SyncPosition
import ntutifm.game.google.entity.sync.SyncRoad
import ntutifm.game.google.entity.sync.SyncSpeed
import ntutifm.game.google.entity.sync.SyncWeather
import ntutifm.game.google.global.MyLog

class ApiProcessor {
    companion object {
        const val getParking = "getParking"
        const val getCityRoadId = "getCityRoadId"
        const val getCityRoadSpeed = "getCityRoadSpeed"
        const val getIncident = "getIncident"
        const val getOil = "getOil"
        const val getWeather = "getWeather"
        const val getWeatherLocation = "getWeatherLocation"
        const val getCameraMark = "getCameraMark"
        const val getFindCamera = "getFindCamera"
        const val getOilStation = "getOilStation"
    }

    suspend fun getParking() {
        val myAPIService = RetrofitManager.api
        val response =
            myAPIService?.parkingList?.execute()
        try {
            if (response?.isSuccessful == true) {
                MyLog.d(response?.body()!!.toString())
                SyncPosition.updateParking(response.body()!!)
            } else {
                Log.d("parkingName", "Null")
            }
        } catch (exception: Exception) {
            Log.d("ParkTitle", exception.toString())
        }
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

    suspend fun getCameraMark() {
        val myAPIService = RetrofitManager.api
        val response = myAPIService?.cameraMark?.execute()
        try {
            if (response?.isSuccessful == true) {
                SyncCamera.updateCameraMark(response.body()!!)
            } else {
                Log.d("getList<Camera>Mark", "Null")
            }
        } catch (exception: Exception) {
            Log.d("getList<Camera>Mark", exception.toString())
        }
    }

    fun getFindCamera(
        c: Context,
        successData: ArrayList<String>,
        errorData: ArrayList<String>,
    ) {
        val myAPIService = RetrofitManager.api
        val response = myAPIService?.getFindCamera(successData[1], successData[2])?.execute()
        try {
            if (response?.isSuccessful == true) {
                MyLog.e("getFindCamera:close")
                SyncCamera.updateFindCamera(response.body()!!)
            } else {
                MyLog.e("getFindCamera:Null")
            }
        } catch (exception: Exception) {
            MyLog.d("getFindCamera:$exception")
        }
    }

    suspend fun getOilStation() {
        val myAPIService = RetrofitManager.api
        val response = myAPIService?.oilStationList?.execute()
        try {
            if (response?.isSuccessful == true) {
                SyncPosition.updateOilStation(response.body()!!)
            } else {
                Log.d("getOilStationMark", "Null")
            }
        } catch (exception: Exception) {
            Log.d("getOilStationMark", exception.toString())
        }
    }
}