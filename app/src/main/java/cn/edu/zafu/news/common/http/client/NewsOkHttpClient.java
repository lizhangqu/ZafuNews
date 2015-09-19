package cn.edu.zafu.news.common.http.client;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by lizhangqu on 2015/5/16.
 */
public class NewsOkHttpClient extends OkHttpClient{
    private volatile static NewsOkHttpClient singleton;

    private NewsOkHttpClient() {
    }

    public static NewsOkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (NewsOkHttpClient.class) {
                if (singleton == null)
                    singleton = new NewsOkHttpClient();
            }

        }
        return singleton;
    }
}
