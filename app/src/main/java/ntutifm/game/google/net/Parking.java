package ntutifm.game.google.net;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Parking {

    @SerializedName("CarParkName")
    private String parkingName ="NAME";
    @SerializedName("PositionLat")
    private Double parkingLat=11.2;
    @SerializedName("PositionLon")
    private Double parkingLng=11.2;

    public String getParkingName() {
        return this.parkingName;
    }
    public Double getLat() {
        return this.parkingLat;
    }
    public Double getLng() {
        return this.parkingLng;
    }

}
