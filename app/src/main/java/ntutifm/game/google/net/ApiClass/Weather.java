package ntutifm.game.google.net.ApiClass;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("locationName")
    private String locationName ="locationName";

    @SerializedName("WeatherDescription")
    private String weatherDescription ="WeatherDescription";

    //降雨機率 per12h
    @SerializedName("PoP12h1")
    private String pop12h1 ="PoP12h1";

    @SerializedName("PoP12h2")
    private String pop12h2 ="PoP12h2";

    @SerializedName("PoP12h3")
    private String pop12h3 ="PoP12h3";

    @SerializedName("PoP12h4")
    private String pop12h4 ="PoP12h4";

    @SerializedName("Wx1")
    private String wx1 ="Wx1";

    @SerializedName("Wx2")
    private String wx2 ="Wx2";

    @SerializedName("Wx3")
    private String wx3 ="Wx3";

    @SerializedName("Wx4")
    private String wx4 ="Wx4";

    @SerializedName("Wx5")
    private String wx5 ="Wx5";

    @SerializedName("Wx6")
    private String wx6 ="Wx6";

    @SerializedName("Wx7")
    private String wx7 ="Wx7";

    @SerializedName("Wx8")
    private String wx8 ="Wx8";

    //Temperature
    @SerializedName("T1")
    private String t1 ="T1";

    @SerializedName("T2")
    private String t2 ="T2";

    @SerializedName("T3")
    private String t3 ="T3";

    @SerializedName("T4")
    private String t4 ="T4";

    @SerializedName("T5")
    private String t5 ="T5";

    @SerializedName("T6")
    private String t6 ="T6";

    @SerializedName("T7")
    private String t7 ="T7";

    @SerializedName("T8")
    private String t8 ="T8";




    public String getLocationName(){
        return this.locationName;
    }
    public String getWeatherDescription() {
        return this.weatherDescription;
    }
    public String getPop12h1() {
        return this.pop12h1;
    }
    public String getPop12h2() {
        return this.pop12h2;
    }
    public String getPop12h3() {
        return this.pop12h3;
    }
    public String getPop12h4() {
        return this.pop12h4;
    }
    public String getWx1() {
        return this.wx1;
    }
    public String getWx2() {
        return this.wx2;
    }
    public String getWx3() {
        return this.wx3;
    }
    public String getWx4() {
        return this.wx4;
    }
    public String getWx5() {
        return this.wx5;
    }
    public String getWx6() {
        return this.wx6;
    }
    public String getWx7() {
        return this.wx7;
    }
    public String getWx8() {
        return this.wx8;
    }
    public String getT1() {
        return this.t1;
    }
    public String getT2() {
        return this.t2;
    }
    public String getT3() {
        return this.t3;
    }
    public String getT4() {
        return this.t4;
    }
    public String getT5() {
        return this.t5;
    }
    public String getT6() {
        return this.t6;
    }
    public String getT7() {
        return this.t7;
    }
    public String getT8() {
        return this.t8;
    }

}
