package cn.edu.zafu.news.net.parser;

import java.io.UnsupportedEncodingException;

/**
 * Created by lizhangqu on 2015/5/16.
 */
public interface Parser<T> {
    T convert(String from) throws UnsupportedEncodingException;
}
