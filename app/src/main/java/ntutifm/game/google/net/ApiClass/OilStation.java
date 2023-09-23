package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class OilStation {
    @SerializedName("Station")
    private String station ="station";
    @SerializedName("Longitude")
    private Double longitude = 123.1;
    @SerializedName("Latitude")
    private Double latitude = 25.2;
    @SerializedName("Address")
    private String address = "address";


    public String getStation() {
        return this.station;
    }
    public String getAddress() {
        return this.address;
    }
    public Double getLatitude() {return this.latitude;}
    public Double getLogitude() {
        return this.longitude;
    }

}
