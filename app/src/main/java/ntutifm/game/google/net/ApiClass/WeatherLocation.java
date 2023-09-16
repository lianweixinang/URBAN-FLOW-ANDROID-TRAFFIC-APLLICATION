package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class WeatherLocation {

    @SerializedName("district")
    private String districtName ="district";

    public String getDistrictName() {
        return this.districtName;
    }
}
