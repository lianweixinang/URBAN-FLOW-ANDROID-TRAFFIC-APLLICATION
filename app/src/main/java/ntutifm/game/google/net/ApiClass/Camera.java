package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class Camera {
    @SerializedName("ID")
    private String Id ="ID";
    @SerializedName("Type")
    private String Type ="Type";
    @SerializedName("Road")
    private String Road ="Road";
    @SerializedName("Session")
    private String Session ="Session";
    @SerializedName("Direction")
    private String Direction ="Direction";
    @SerializedName("Limit")
    private String Limit ="Limit";
    @SerializedName("Latitude")
    private String Latitude ="Latitude";
    @SerializedName("Longitude")
    private String Longitude ="Longitude";

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
    public String getLatitude() {return this.Latitude;}
    public String getLongitude() {return this.Longitude;}


}
