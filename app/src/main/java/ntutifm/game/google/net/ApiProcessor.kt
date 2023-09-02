package ntutifm.game.google.net

import android.content.Context
import android.util.Log
import ntutifm.game.google.entity.*
import ntutifm.game.google.entity.SyncOil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiClass.*
import ntutifm.game.google.ui.map.mClusterManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class ApiProcessor {
    companion object{
    public val getParking = "getParking"
    public val getCityRoadId = "getCityRoadId"
    public val getCityRoadSpeed = "getCityRoadSpeed"
    public val getIncident = "getIncident"
    public val getOil = "getOil"}

    fun getParking(c: Context, successData:ArrayList<String> , errorData:ArrayList<String>){
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<Parking>>? = myAPIService.parkingList
        call!!.enqueue(object : Callback<List<Parking>> {
            override fun onResponse(
                call: Call<List<Parking>>?,
                response: Response<List<Parking>>?
            ) {
                if(response?.body()!= null){
                    for (item in response.body()!!){
                        mClusterManager?.addItem(MyItem(item.lat, item.lng)) //要傳參數嗎
                    }
                }else{
                    Log.d("parkingName", "Null")
                }
            }
            override fun onFailure(call: Call<List<Parking>>?, t: Throwable?) {
                Log.d("ParkTitle", t.toString())
            }
        })
    }
    fun getCityRoadId(c: Context, successData:ArrayList<String> , errorData:ArrayList<String>){
        val myAPIService = RetrofitManager.getInstance().api
        MyLog.d(successData.toString())
        val call: Call<List<CityRoad>>? = myAPIService.getRoadId(successData[1])
        call!!.enqueue(object : Callback<List<CityRoad>> {
            override fun onResponse(
                call: Call<List<CityRoad>>?,
                response: Response<List<CityRoad>>?
            ) {
                if(response?.body()!= null){
                    MyLog.d("SearchStart")
                    SyncRoad.updateSearch(response.body()!!)
                }else{
                    Log.d("RoadName", "Null")
                }
            }
            override fun onFailure(call: Call<List<CityRoad>>?, t: Throwable?) {
                Log.d("RoadName", t.toString())
            }
        })
    }
    fun getCityRoadSpeed(c: Context, successData:ArrayList<String> , errorData:ArrayList<String>){
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<CitySpeed>>? = myAPIService.getRoadSpeed(successData[1])
        call!!.enqueue(object : Callback<List<CitySpeed>> {
            override fun onResponse(
                call: Call<List<CitySpeed>>?,
                response: Response<List<CitySpeed>>?
            ) {
                if(response?.body()!= null){
                    MyLog.d(response?.body()!!.toString())
                   SyncSpeed.updateSpeed(response?.body()!!)
                }else{
                    Log.d("RoadSpeed", "Null")
                }
            }
            override fun onFailure(call: Call<List<CitySpeed>>?, t: Throwable?) {
                Log.d("RoadSpeed", t.toString())
            }
        })
    }
        fun getIncident(c: Context, successData:ArrayList<String> , errorData:ArrayList<String>){
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<Incident>>? = myAPIService.incidentList
        call!!.enqueue(object : Callback<List<Incident>> {
            override fun onResponse(
                call: Call<List<Incident>>?,
                response: Response<List<Incident>>?
            ) {
                if(response?.body()!= null){
                    MyLog.d(response?.body()!!.toString())
                    SyncIncident.updateIncident(response?.body()!!)
                }else{
                    Log.d("parkingName", "Null")
                }
            }
            override fun onFailure(call: Call<List<Incident>>?, t: Throwable?) {
                Log.d("Title", t.toString())
            }
        })
    }
    fun getOil(c: Context, successData:ArrayList<String> , errorData:ArrayList<String>){
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<Oil>>? = myAPIService.oilList
        call!!.enqueue(object : Callback<List<Oil>> {
            override fun onResponse(
                call: Call<List<Oil>>?,
                response: Response<List<Oil>>?
            ) {
                if(response?.body()!= null){
                    MyLog.d(response?.body()!!.toString())
                    SyncOil.updateOil(response?.body()!!)
                }else{
                    MyLog.d("getOil")
                }
            }
            override fun onFailure(call: Call<List<Oil>>?, t: Throwable?) {
                Log.d("Title", t.toString())
            }
        })
    }
}