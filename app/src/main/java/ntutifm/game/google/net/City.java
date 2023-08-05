package ntutifm.game.google.net;

public class City {

    private String cityID;
    private String cityName;
    private String cityCode;
    private String city;
    private String countryID;
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
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }

    //public LatLng getLatLng() {return LatLng(this.City)}

}