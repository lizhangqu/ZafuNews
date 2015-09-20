package cn.edu.zafu.news.model;

/**
 * Created by lizhangqu on 2015/5/16.
 */

public class NewsContent{
    public static final int TYPE_TITLE=0x01;
    public static final int TYPE_INFO=0x02;
    public static final int TYPE_IMAGE=0x03;
    public static final int TYPE_PARAM=0x04;
    private int type;
    private String content;

    public NewsContent() {
    }

    public NewsContent(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NewsContent{" +
                "type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
