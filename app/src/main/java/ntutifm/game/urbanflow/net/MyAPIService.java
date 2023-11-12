package ntutifm.game.urbanflow.net;

import java.util.List;

import ntutifm.game.urbanflow.apiClass.Camera;
import ntutifm.game.urbanflow.apiClass.City;
import ntutifm.game.urbanflow.apiClass.CitySpeed;
import ntutifm.game.urbanflow.apiClass.Incident;
import ntutifm.game.urbanflow.apiClass.Oil;
import ntutifm.game.urbanflow.apiClass.OilStation;
import ntutifm.game.urbanflow.apiClass.Parking;
import ntutifm.game.urbanflow.apiClass.SearchHistory;
import ntutifm.game.urbanflow.apiClass.Weather;
import ntutifm.game.urbanflow.apiClass.WeatherLocation;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MyAPIService {

    @GET("city")
    Call<List<City>> getCityList();
    @GET("findCamera/{lat},{lng}")
    Call<Camera> getFindCamera(@Path("lat") String lat, @Path("lng") String lng);
    @GET("cameraMark")
    Call<List<Camera>> getCameraMark();
    @GET("parking")
    Call<List<Parking>> getParkingList();
    @GET("cityRoad/{roadName}")
    Call<List<SearchHistory>> getRoadId(@Path("roadName") String roadName);
    @GET("citySpeed/{roadId}")
    Call<List<CitySpeed>> getRoadSpeed(@Path("roadId") String roadId);
    @GET("incident")
    Call<List<Incident>> getIncidentList();
    @GET("oil")
    Call<List<Oil>> getOilList();
    @GET("weather")
    Call<List<Weather>> getWeatherList();
    @GET("oilStation")
    Call<List<OilStation>> getOilStationList();
    @GET("weatherLocation/{lat},{lng}")
    Call<WeatherLocation> getWeatherLocationLatLng(@Path("lat") String lat, @Path("lng") String lng);

}