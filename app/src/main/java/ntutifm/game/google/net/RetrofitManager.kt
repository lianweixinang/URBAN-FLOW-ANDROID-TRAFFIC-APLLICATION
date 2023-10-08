package ntutifm.game.google.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {
    companion object {
        private var instance: RetrofitManager? = null
        private var _api: MyAPIService? = null
        val api get() = _api
        init{
            if (instance == null) {
                instance = RetrofitManager()
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://ntutifm.zeabur.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                _api = retrofit.create(MyAPIService::class.java)
            }
        }
    }
}