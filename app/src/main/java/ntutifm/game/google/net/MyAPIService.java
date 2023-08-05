package ntutifm.game.google.net;

import java.util.List;

import ntutifm.game.google.net.ApiClass.City;
import ntutifm.game.google.net.ApiClass.Parking;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MyAPIService {

    @GET("city")
    Call<List<City>> getCityList();
    @GET("parking")
    Call<List<Parking>> getParkingList();
}
