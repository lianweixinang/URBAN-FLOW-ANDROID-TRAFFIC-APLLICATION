package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class CityRoad {

    @SerializedName("RoadId")
    private String roadId ="ID";
    @SerializedName("RoadName")
    private String roadName="NAME";
    public CityRoad(String id, String name){
        this.roadId = id;
        this.roadName = name;
    }


    public String getRoadName() {
        return this.roadName;
    }
    public String getRoadId() {
        return this.roadId;
    }

}
