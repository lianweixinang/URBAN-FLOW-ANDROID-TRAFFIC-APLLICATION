package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class Camera {
    @SerializedName("ID")
    private String Id ="ID";
    @SerializedName("Type")
    private String Type ="Type";
    @SerializedName("Road")
    private String Road ="Road";
    @SerializedName("Introduction")
    private String Introduction ="Introduction";
    @SerializedName("Session")
    private String Session ="Session";
    @SerializedName("Direction")
    private String Direction ="Direction";
    @SerializedName("Limit")
    private String Limit ="Limit";
    @SerializedName("Latitude")
    private Double Latitude = 25.0;
    @SerializedName("Longitude")
    private Double Longitude = 123.0;
    @SerializedName("Distance")
    private Integer Distance = 10000;

    public Camera(){
    }
    public String getId() {
        return this.Id;
    }
    public String getType() {return this.Type;}
    public String getRoad() {
        return this.Road;
    }
    public String getSession() {
        return this.Session;
    }
    public String getDirection() {
        return this.Direction;
    }
    public String getLimit() {return this.Limit;}
    public Double getLatitude() {return this.Latitude;}
    public Double getLongitude() {return this.Longitude;}
    public String getIntroduction(){return this.Introduction;}
    public Integer getDistance(){return this.Distance;}

}
