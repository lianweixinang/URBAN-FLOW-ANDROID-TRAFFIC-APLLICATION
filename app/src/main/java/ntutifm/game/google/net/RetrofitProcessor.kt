package ntutifm.game.google.net

import android.util.Log
import ntutifm.game.google.net.ApiClass.Parking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitProcessor {


    fun get() {
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<Parking>>? = myAPIService.parkingList
        call!!.enqueue(object : Callback<List<Parking>> {
            override fun onResponse(
                call: Call<List<Parking>>?,
                response: Response<List<Parking>>?
            ) {
                if (response?.body() != null) {
                    for (item in response?.body()!!) {
                        //val position: LatLng = item.latLng
                        //mClusterManager?.addItem(MyItem(position.latitude , position.longitude))
                    }
                } else {
                    Log.d("parkingName", "Null")
                }
            }

            override fun onFailure(call: Call<List<Parking>>?, t: Throwable?) {
                Log.d("title", t.toString())
            }
        })
    }
}