package cn.edu.zafu.news.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by lizhangqu on 2015/5/17.
 */
@Table(name="history")
public class History extends Model {
    @Column(name = "title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public History(){
    }
    public History(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "History{" +
                "title='" + title + '\'' +
                '}';
    }
}
