package ntutifm.game.google.net;

import java.util.List;

import ntutifm.game.google.net.ApiClass.City;
import ntutifm.game.google.net.ApiClass.CitySpeed;
import ntutifm.game.google.net.ApiClass.Incident;
import ntutifm.game.google.net.ApiClass.Parking;
import ntutifm.game.google.net.ApiClass.CityRoad;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MyAPIService {

    @GET("city")
    Call<List<City>> getCityList();
    @GET("parking")
    Call<List<Parking>> getParkingList();
    @GET("cityRoad/{roadName}")
    Call<List<CityRoad>> getRoadId(@Path("roadName") String roadName);
    @GET("citySpeed/{roadId}")
    Call<List<CitySpeed>> getRoadSpeed(@Path("roadId") String roadId);
    @GET("incident")
    Call<List<Incident>> getIncidentList();
}
