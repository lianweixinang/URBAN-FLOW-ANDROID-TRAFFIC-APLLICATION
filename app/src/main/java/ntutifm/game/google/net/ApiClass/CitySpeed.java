package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class CitySpeed {

    @SerializedName("Direction")
    private String direction ="Direction";
    @SerializedName("AvgSpeed")
    private Double avgSpeed=0.0;
    @SerializedName("Volume")
    private Integer volume=-99;


    public String getDirection() {
        return this.direction;
    }
    public Double getAvgSpeed() {
        return this.avgSpeed;
    }
    public Integer getVolume() {
        return this.volume;
    }
}
