package cn.edu.zafu.news.model.weather;

import java.io.Serializable;

/**
 * Created by lizhangqu on 2015/5/29.
 */
public class Today implements Serializable{
    private String cityCode;
    private String date;
    private int humidityMax;
    private int humidityMin;
    private int precipitationMax;
    private int precipitationMin;
    private int tempMax;
    private int tempMin;

    @Override
    public String toString() {
        return "Today{" +
                "cityCode='" + cityCode + '\'' +
                ", date='" + date + '\'' +
                ", humidityMax=" + humidityMax +
                ", humidityMin=" + humidityMin +
                ", precipitationMax=" + precipitationMax +
                ", precipitationMin=" + precipitationMin +
                ", tempMax=" + tempMax +
                ", tempMin=" + tempMin +
                ", weatherEnd='" + weatherEnd + '\'' +
                ", weatherStart='" + weatherStart + '\'' +
                ", windDirectionEnd='" + windDirectionEnd + '\'' +
                ", windDirectionStart='" + windDirectionStart + '\'' +
                ", windMax=" + windMax +
                ", windMin=" + windMin +
                '}';
    }

    private String weatherEnd;
    private String weatherStart;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHumidityMax() {
        return humidityMax;
    }

    public void setHumidityMax(int humidityMax) {
        this.humidityMax = humidityMax;
    }

    public int getHumidityMin() {
        return humidityMin;
    }

    public void setHumidityMin(int humidityMin) {
        this.humidityMin = humidityMin;
    }

    public int getPrecipitationMax() {
        return precipitationMax;
    }

    public void setPrecipitationMax(int precipitationMax) {
        this.precipitationMax = precipitationMax;
    }

    public int getPrecipitationMin() {
        return precipitationMin;
    }

    public void setPrecipitationMin(int precipitationMin) {
        this.precipitationMin = precipitationMin;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public String getWeatherEnd() {
        return weatherEnd;
    }

    public void setWeatherEnd(String weatherEnd) {
        this.weatherEnd = weatherEnd;
    }

    public String getWeatherStart() {
        return weatherStart;
    }

    public void setWeatherStart(String weatherStart) {
        this.weatherStart = weatherStart;
    }

    public String getWindDirectionEnd() {
        return windDirectionEnd;
    }

    public void setWindDirectionEnd(String windDirectionEnd) {
        this.windDirectionEnd = windDirectionEnd;
    }

    public String getWindDirectionStart() {
        return windDirectionStart;
    }

    public void setWindDirectionStart(String windDirectionStart) {
        this.windDirectionStart = windDirectionStart;
    }

    public int getWindMax() {
        return windMax;
    }

    public void setWindMax(int windMax) {
        this.windMax = windMax;
    }

    public int getWindMin() {
        return windMin;
    }

    public void setWindMin(int windMin) {
        this.windMin = windMin;
    }

    private String windDirectionEnd;
    private String windDirectionStart;
    private int windMax;
    private int windMin;
}
