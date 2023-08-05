package ntutifm.game.google.net;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Parking {

    private String parkingName;

    private Double parkingLng;

    private Double parkingLat;


    public Parking(String CarParkName, Double PositionLat, Double PositionLon) {
        this.parkingName = "xxxx";
        Log.e("xxxx",PositionLat.toString()+PositionLon.toString());
        this.parkingLng = 12.11;
        this.parkingLat = 12.11;
    }

    public String getParkingName() {
        return "XXXX";
    }

    public LatLng getLatLng() {
        Log.e("xxxx",parkingName);
        return new LatLng(112.1, 112.1);}

}
