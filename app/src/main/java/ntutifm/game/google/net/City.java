package ntutifm.game.google.net;

public class City {

    private String CityID;
    private String CityName;
    private String CityCode;
    private String City;
    private String CountryID;
    private String Version;

    public City(String CityID, String CityName, String  CityCode,
                  String City, String CountryID, String Version) {
        this.CityID = CityID;
        this.CityName = CityName;
        this.CityCode = CityCode;
        this.City = City;
        this.CountryID = CountryID;
        this.Version = Version;
    }

    public String getCityID() {
        return CityID;
    }

    public String getCityName() {
        return CityName;
    }
}