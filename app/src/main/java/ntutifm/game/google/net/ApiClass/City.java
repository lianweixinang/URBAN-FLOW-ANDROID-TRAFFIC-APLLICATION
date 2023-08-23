package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("CityID")
    private String cityID;
    @SerializedName("CityName")
    private String cityName;
    @SerializedName("CityCode")
    private String cityCode;
    @SerializedName("City")
    private String city;
    @SerializedName("CountryID")
    private String countryID;
    @SerializedName("Version")
    private String version;

    public City(String CityID, String CityName, String  CityCode,
                  String City, String CountryID, String Version) {
        this.cityID = CityID;
        this.cityName = CityName;
        this.cityCode = CityCode;
        this.city = City;
        this.countryID = CountryID;
        this.version = Version;
    }

    public String getCityID() {
        return this.cityID;
    }
    public String getCityName() {
        return this.cityName;
    }
    public String getCityCode() {
        return this.cityCode;
    }
    public String getCity() {
        return this.city;
    }
    public String getCountryID() {
        return this.countryID;
    }
    public String getVersion() {
        return this.version;
    }





    //public LatLng getLatLng() {return LatLng(this.City)}

}