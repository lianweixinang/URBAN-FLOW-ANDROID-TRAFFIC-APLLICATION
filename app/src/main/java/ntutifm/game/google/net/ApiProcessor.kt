package ntutifm.game.google.net

import android.content.Context
import android.util.Log
import ntutifm.game.google.entity.MyItem
import ntutifm.game.google.net.ApiClass.Parking
import ntutifm.game.google.ui.map.mClusterManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class ApiProcessor {
    public val getParking = "getParking"

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
                Log.d("title", t.toString())
            }
        })
    }
}