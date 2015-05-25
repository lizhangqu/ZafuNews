package cn.edu.zafu.news.common.parser.impl;

import android.util.Log;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import cn.edu.zafu.news.common.parser.Parser;
import cn.edu.zafu.news.model.Update;

/**
 * Created by lizhangqu on 2015/5/24.
 */
public class UpdateParser implements Parser<Update> {
    private static Gson gson = new Gson();

    @Override
    public Update convert(String from) throws UnsupportedEncodingException {
        Update update = gson.fromJson(from, Update.class);
        Log.d("TAG", "更新信息：" + update.toString());
        return update;
    }
}
