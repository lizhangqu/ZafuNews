package cn.edu.zafu.news.net.parser.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zafu.news.net.parser.Parser;
import cn.edu.zafu.news.db.model.NewsItem;

/**
 * Created by lizhangqu on 2015/5/16.
 */
public class NewsParser implements Parser<List<NewsItem>> {
    @Override
    public List<NewsItem> convert(String from) {
        List<NewsItem> lists = new ArrayList<NewsItem>();
        Document document = Jsoup.parse(from);
        Elements select = document.select("div.content-right ul li");
        String title = null;
        String author = null;
        String click = null;
        for (Element element : select) {
            NewsItem item = new NewsItem();
            Elements timeElement = element.select("span");
            item.setTime(timeElement.text());
            Elements titleElement = element.select("a");
            String[] split = titleElement.attr("title").split("\n");
            try {
                if (split.length > 1) {
                    title = split[0].substring(split[0].indexOf("：") + 1);
                } else {
                    title = titleElement.text();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                author = split[1].substring(split[1].indexOf("：") + 1);
            } catch (ArrayIndexOutOfBoundsException e) {
                author = "";
            }
            try {
                click = split[2].substring(split[2].indexOf("：") + 1);
            } catch (ArrayIndexOutOfBoundsException e) {
                click = "未知";
            }

            item.setTitle(title);
            item.setAuthor("".equals(author) ? "无" : author);
            item.setClick(click);
            if(titleElement.attr("href").startsWith("/")){
                item.setUrl("http://news.zafu.edu.cn" + titleElement.attr("href"));
            }else{
                item.setUrl(titleElement.attr("href"));
            }

            lists.add(item);
        }
        return lists;
    }
}
