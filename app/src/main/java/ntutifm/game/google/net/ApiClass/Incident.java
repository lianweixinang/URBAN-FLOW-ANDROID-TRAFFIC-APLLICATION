package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class Incident {

    @SerializedName("Type")
    private String type ="Type";
    @SerializedName("Solved")
    private String solved ="Solved";
    @SerializedName("Edited_Time")
    private String edited_time ="Edited_Time";
    @SerializedName("Raise_Time")
    private String raise_time ="Raise_Time";
    @SerializedName("Auth")
    private String auth ="Auth";
    @SerializedName("Part")
    private String part ="Part";
    @SerializedName("Title")
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
