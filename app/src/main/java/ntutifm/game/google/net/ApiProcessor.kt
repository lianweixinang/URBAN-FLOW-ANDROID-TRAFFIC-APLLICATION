package ntutifm.game.google.net

import android.content.Context
import android.util.Log
import ntutifm.game.google.entity.*
import ntutifm.game.google.entity.SyncOil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiClass.*
import ntutifm.game.google.net.ApiClass.Parking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun getParking(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<Parking>>? = myAPIService.parkingList
        call!!.enqueue(object : Callback<List<Parking>> {
            override fun onResponse(
                call: Call<List<Parking>>?,
                response: Response<List<Parking>>?,
            ) {
                if (response?.body() != null) {
                    SyncPosition.updateParking(response.body()!!)
                } else {
                    Log.d("parkingName", "Null")
                }
            }

            override fun onFailure(call: Call<List<Parking>>?, t: Throwable?) {
                Log.d("ParkTitle", t.toString())
            }
        })
    }

    fun getCityRoadId(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.getInstance().api
        MyLog.d(successData.toString())
        val call: Call<List<CityRoad>>? = myAPIService.getRoadId(successData[1])
        call!!.enqueue(object : Callback<List<CityRoad>> {
            override fun onResponse(
                call: Call<List<CityRoad>>?,
                response: Response<List<CityRoad>>?,
            ) {
                if (response?.body() != null) {
                    MyLog.d("SearchStart")
                    SyncRoad.updateSearch(response.body()!!)
                } else {
                    Log.d("RoadName", "Null")
                }
            }

            override fun onFailure(call: Call<List<CityRoad>>?, t: Throwable?) {
                Log.d("RoadName", t.toString())
            }
        })
    }

    fun getCityRoadSpeed(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<CitySpeed>>? = myAPIService.getRoadSpeed(successData[1])
        call!!.enqueue(object : Callback<List<CitySpeed>> {
            override fun onResponse(
                call: Call<List<CitySpeed>>?,
                response: Response<List<CitySpeed>>?,
            ) {
                if (response?.body() != null) {
                    MyLog.d("enterApi")
                    MyLog.d(response?.body()!!.toString())
                    SyncSpeed.updateSpeed(response?.body()!!)
                } else {
                    Log.d("RoadSpeed", "Null")
                }
            }

            override fun onFailure(call: Call<List<CitySpeed>>?, t: Throwable?) {
                Log.d("RoadSpeed", t.toString())
            }
        })
    }

    fun getIncident(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<Incident>>? = myAPIService.incidentList
        call!!.enqueue(object : Callback<List<Incident>> {
            override fun onResponse(
                call: Call<List<Incident>>?,
                response: Response<List<Incident>>?,
            ) {
                if (response?.body() != null) {
                    MyLog.d(response?.body()!!.toString())
                    SyncIncident.updateIncident(response?.body()!!)
                } else {
                    Log.d("parkingName", "Null")
                }
            }

            override fun onFailure(call: Call<List<Incident>>?, t: Throwable?) {
                Log.d("Title", t.toString())
            }
        })
    }

    fun getOil(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<Oil>>? = myAPIService.oilList
        call!!.enqueue(object : Callback<List<Oil>> {
            override fun onResponse(
                call: Call<List<Oil>>?,
                response: Response<List<Oil>>?,
            ) {
                if (response?.body() != null) {
                    MyLog.d(response?.body()!!.toString())
                    SyncOil.updateOil(response?.body()!!)
                } else {
                    MyLog.d("getOil")
                }
            }

            override fun onFailure(call: Call<List<Oil>>?, t: Throwable?) {
                Log.d("Title", t.toString())
            }
        })
    }

    fun getWeather(c: Context, successData: ArrayList<String>, errorData: ArrayList<String>) {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<Weather>>? = myAPIService.weatherList
        call!!.enqueue(object : Callback<List<Weather>> {
            override fun onResponse(
                call: Call<List<Weather>>?,
                response: Response<List<Weather>>?,
            ) {
                if (response?.body() != null) {
                    MyLog.d("startWeatherApi")
                    MyLog.d(response?.body()!!.toString())
                    SyncWeather.updateWeather(response?.body()!!)
                } else {
                    MyLog.d("getWeatherZero")
                }
            }

            override fun onFailure(call: Call<List<Weather>>?, t: Throwable?) {
                Log.d("Title", t.toString())
            }
        })
    }

    fun getWeatherLocation(
        c: Context,
        successData: ArrayList<String>,
        errorData: ArrayList<String>
    ) {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<WeatherLocation>? =
            myAPIService.getWeatherLocationLatLng(successData[1], successData[2])
        call!!.enqueue(object : Callback<WeatherLocation> {
            override fun onResponse(
                call: Call<WeatherLocation>?,
                response: Response<WeatherLocation>?,
            ) {
                if (response?.body() != null) {
                    SyncPosition.updateWeatherLocation(response.body()!!)
                } else {
                    Log.d("district", "Null")
                }
            }

            override fun onFailure(call: Call<WeatherLocation>?, t: Throwable?) {
                Log.d("DistrictTitle", t.toString())
            }
        })
    }
    fun getCameraMark(
        c: Context,
        successData: ArrayList<String>,
        errorData: ArrayList<String>
    ) {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<Camera>>? = myAPIService.cameraMark
        call!!.enqueue(object : Callback<List<Camera>> {
            override fun onResponse(
                call: Call<List<Camera>>?,
                response: Response<List<Camera>>?,
            ) {
                if (response?.body() != null) {
                    SyncCamera.updateCameraMark(response.body()!!)
                } else {
                    Log.d("getList<Camera>Mark", "Null")
                }
            }

            override fun onFailure(call: Call<List<Camera>>?, t: Throwable?) {
                Log.d("getList<Camera>Mark", t.toString())
            }
        })
    }

    fun getFindCamera(
        c: Context,
        successData: ArrayList<String>,
        errorData: ArrayList<String>
    ) {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<Camera>? =
            myAPIService.getFindCamera(successData[1], successData[2])
        call!!.enqueue(object : Callback<Camera> {
            override fun onResponse(
                call: Call<Camera>?,
                response: Response<Camera>?,
            ) {
                if (response?.body() != null) {
                    MyLog.e("getFindCamera:close")
                    SyncCamera.updateFindCamera(response.body()!!)
                } else {
                    MyLog.e("getFindCamera:Null")
                }
            }

            override fun onFailure(call: Call<Camera>?, t: Throwable?) {
                MyLog.d("getFindCamera:${t}")
            }
        })
    }
    fun getOilStation(
        c: Context,
        successData: ArrayList<String>,
        errorData: ArrayList<String>
    ) {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<OilStation>>? =
            myAPIService.oilStationList
        call!!.enqueue(object : Callback<List<OilStation>> {
            override fun onResponse(
                call: Call<List<OilStation>>?,
                response: Response<List<OilStation>>?,
            ) {
                if (response?.body() != null) {
                    SyncPosition.updateOilStation(response.body()!!)
                } else {
                    Log.d("getOilStationMark", "Null")
                }
            }

            override fun onFailure(call: Call<List<OilStation>>?, t: Throwable?) {
                Log.d("getOilStationMark", t.toString())
            }
        })
    }
}