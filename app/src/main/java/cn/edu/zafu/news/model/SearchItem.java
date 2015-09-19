package cn.edu.zafu.news.model;

import java.io.Serializable;

/**
 * Created by lizhangqu on 2015/5/15.
 */

public class SearchItem implements Serializable{
    private String title;
    private String category;
    private String time;
    private String url;
    public SearchItem() {
    }

    public SearchItem(String title, String category, String time, String url) {
        this.title = title;
        this.category = category;
        this.time = time;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SearchItem{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
