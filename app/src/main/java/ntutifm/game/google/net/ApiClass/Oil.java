package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class Oil {
    @SerializedName("date")
    private String date ="date";
    @SerializedName("92")
    private String ninetwo ="92";
    @SerializedName("95")
    private String ninefive ="95";
    @SerializedName("98")
    private String nineeight ="98";
    @SerializedName("super")
    private String superoil ="super";

    public String getDate() {
        return this.date;
    }
    public String getNinetwo() {return this.ninetwo;}
    public String getNinefive() {
        return this.ninefive;
    }
    public String getNineeight() {
        return this.nineeight;
    }
    public String getSuperoil() {
        return this.superoil;
    }

}
