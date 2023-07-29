package ntutifm.game.google.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyAPIService {
    @GET("city")
    Call<City> getCity();
    @GET("city")
    Call<List<City>> getCityList();
}
