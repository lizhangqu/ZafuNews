package cn.edu.zafu.news.common.parser.impl;

import android.util.Log;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import cn.edu.zafu.news.common.parser.Parser;
import cn.edu.zafu.news.model.weather.Weather;

/**
 * Created by lizhangqu on 2015/5/29.
 */
public class WeatherParser implements Parser<Weather> {
    private static Gson gson = new Gson();
    @Override
    public Weather convert(String from) throws UnsupportedEncodingException {
        Weather weather = gson.fromJson(from, Weather.class);
        Log.d("TAG", "weather:" + weather.toString());
        return weather;
    }
}
