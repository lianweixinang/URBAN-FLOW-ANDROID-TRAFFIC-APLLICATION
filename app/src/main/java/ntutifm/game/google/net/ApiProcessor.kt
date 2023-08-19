package ntutifm.game.google.net

import android.content.Context
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ntutifm.game.google.MyActivity
import ntutifm.game.google.entity.MyItem
import ntutifm.game.google.entity.SearchData
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiClass.CityRoad
import ntutifm.game.google.net.ApiClass.CitySpeed
import ntutifm.game.google.net.ApiClass.Parking
import ntutifm.game.google.ui.map.behavior
import ntutifm.game.google.ui.map.mClusterManager
import ntutifm.game.google.ui.search.SearchViewModel
import ntutifm.game.google.ui.search.searchList
import ntutifm.game.google.ui.search.speedData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class ApiProcessor {
    public val getParking = "getParking"
    public val getCityRoadId = "getCityRoadId"
    public val getCityRoadSpeed = "getCityRoadSpeed"

    public fun getParking(c: Context, successData:ArrayList<String> , errorData:ArrayList<String>){
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
    public fun getCityRoadId(c: Context, successData:ArrayList<String> , errorData:ArrayList<String>){
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<CityRoad>>? = myAPIService.getRoadId(successData[1])
        call!!.enqueue(object : Callback<List<CityRoad>> {
            override fun onResponse(
                call: Call<List<CityRoad>>?,
                response: Response<List<CityRoad>>?
            ) {
                if(response?.body()!= null){
                    MyLog.d("SearchStart")
                    SearchViewModel().updateSearch(response.body()!!)
                    searchList.clear()
                    searchList.addAll(response.body()!!)
                }else{
                    Log.d("RoadName", "Null")
                }
            }
            override fun onFailure(call: Call<List<CityRoad>>?, t: Throwable?) {
                Log.d("RoadName", t.toString())
            }
        })
    }
    public fun getCityRoadSpeed(c: Context, successData:ArrayList<String> , errorData:ArrayList<String>){
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<CitySpeed>>? = myAPIService.getRoadSpeed(successData[1])
        call!!.enqueue(object : Callback<List<CitySpeed>> {
            override fun onResponse(
                call: Call<List<CitySpeed>>?,
                response: Response<List<CitySpeed>>?
            ) {
                if(response?.body()!= null){
                    speedData.clear()
                    speedData.addAll(response.body()!!)
                }else{
                    Log.d("RoadSpeed", "Null")
                }
            }
            override fun onFailure(call: Call<List<CitySpeed>>?, t: Throwable?) {
                Log.d("RoadSpeed", t.toString())
            }
        })
    }
}