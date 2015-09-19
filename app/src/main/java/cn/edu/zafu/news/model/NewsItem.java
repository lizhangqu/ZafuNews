package cn.edu.zafu.news.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by lizhangqu on 2015/5/15.
 */
@Table(name = "news_item")
public class NewsItem extends Model implements Serializable{
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "time")
    private String time;
    @Column(name = "click")
    private String click;
    @Column(name = "url")
    private String url;

    public NewsItem() {
    }

    public NewsItem(String title, String author, String time, String click, String url) {
        this.title = title;
        this.author = author;
        this.time = time;
        this.click = click;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", time='" + time + '\'' +
                ", click='" + click + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
