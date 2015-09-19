package cn.edu.zafu.news.model;

import java.io.Serializable;

/**
 * Created by lizhangqu on 2015/5/15.
 */

public class Category implements Serializable{
    private String title;
    private String url;

    public Category() {
    }

    public Category(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Category{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
