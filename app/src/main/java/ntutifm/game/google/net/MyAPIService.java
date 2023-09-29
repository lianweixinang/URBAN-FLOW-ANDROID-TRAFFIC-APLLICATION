package ntutifm.game.google.net;

import java.util.List;

import ntutifm.game.google.net.apiClass.Camera;
import ntutifm.game.google.net.apiClass.City;
import ntutifm.game.google.net.apiClass.CitySpeed;
import ntutifm.game.google.net.apiClass.Incident;
import ntutifm.game.google.net.apiClass.Oil;
import ntutifm.game.google.net.apiClass.OilStation;
import ntutifm.game.google.net.apiClass.Parking;
import ntutifm.game.google.net.apiClass.CityRoad;
import ntutifm.game.google.net.apiClass.Weather;
import ntutifm.game.google.net.apiClass.WeatherLocation;
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
    Call<List<CityRoad>> getRoadId(@Path("roadName") String roadName);
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