package cn.edu.zafu.news.model.weather;

import java.io.Serializable;

/**
 * Created by lizhangqu on 2015/5/29.
 */
public class RealTime implements Serializable{
    private String SD;
    private String WD;
    private String WS;
    private String WSE;
    private String city;
    private String cityid;
    private String isRadar;
    private String radar;
    private String temp;
    private String time;
    private String weather;

    public String getSD() {
        return SD;
    }

    public void setSD(String SD) {
        this.SD = SD;
    }

    public String getWD() {
        return WD;
    }

    public void setWD(String WD) {
        this.WD = WD;
    }

    public String getWS() {
        return WS;
    }

    public void setWS(String WS) {
        this.WS = WS;
    }

    public String getWSE() {
        return WSE;
    }

    public void setWSE(String WSE) {
        this.WSE = WSE;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getIsRadar() {
        return isRadar;
    }

    public void setIsRadar(String isRadar) {
        this.isRadar = isRadar;
    }

    public String getRadar() {
        return radar;
    }

    public void setRadar(String radar) {
        this.radar = radar;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "RealTime{" +
                "SD='" + SD + '\'' +
                ", WD='" + WD + '\'' +
                ", WS='" + WS + '\'' +
                ", WSE='" + WSE + '\'' +
                ", city='" + city + '\'' +
                ", cityid='" + cityid + '\'' +
                ", isRadar='" + isRadar + '\'' +
                ", radar='" + radar + '\'' +
                ", temp='" + temp + '\'' +
                ", time='" + time + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }
}
