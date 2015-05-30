package cn.edu.zafu.news.model.weather;

import java.io.Serializable;

/**
 * Created by lizhangqu on 2015/5/29.
 */
public class Weather implements Serializable{
    private RealTime realtime;
    private Today today;

    public RealTime getRealtime() {
        return realtime;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "realtime=" + realtime +
                ", today=" + today +
                '}';
    }

    public void setRealtime(RealTime realtime) {
        this.realtime = realtime;
    }

    public Today getToday() {
        return today;
    }

    public void setToday(Today today) {
        this.today = today;
    }
}
