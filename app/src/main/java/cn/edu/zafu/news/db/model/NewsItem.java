package cn.edu.zafu.news.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by lizhangqu on 2015/5/15.
 */
@DatabaseTable(tableName = "news_item")
public class NewsItem  implements Serializable{
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "author")
    private String author;
    @DatabaseField(columnName = "time")
    private String time;
    @DatabaseField(columnName = "click")
    private String click;
    @DatabaseField(columnName = "url")
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", time='" + time + '\'' +
                ", click='" + click + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
