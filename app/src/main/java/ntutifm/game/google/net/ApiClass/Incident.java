package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class Incident {

    @SerializedName("type")
    private String type ="Type";
    @SerializedName("solved")
    private String solved ="Solved";
    @SerializedName("edited_time")
    private String edited_time ="Edited_Time";
    @SerializedName("raise_time")
    private String raise_time ="Raise_Time";
    @SerializedName("auth")
    private String auth ="Auth";
    @SerializedName("part")
    private String part ="Part";
    @SerializedName("title")
    private String title ="Title";

    public String getType() {
        return this.type;
    }
    public String getSolved() {
        return this.solved;
    }
    public String getEdited_Time() {
        return this.edited_time;
    }
    public String getRaise_Time() {
        return this.raise_time;
    }
    public String getAuth() {
        return this.auth;
    }
    public String getPart() {
        return this.part;
    }
    public String getTitle() {
        return this.title;
    }

}
