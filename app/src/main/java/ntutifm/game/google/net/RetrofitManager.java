package ntutifm.game.google.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    // 以Singleton模式建立
    private static RetrofitManager mInstance = new RetrofitManager();
    private MyAPIService myAPIService;
    private RetrofitManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.178:80")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myAPIService = retrofit.create(MyAPIService.class);
    }

    public static RetrofitManager getInstance() {
        return mInstance;
    }

    public MyAPIService getAPI() {
        return myAPIService;
    }
}