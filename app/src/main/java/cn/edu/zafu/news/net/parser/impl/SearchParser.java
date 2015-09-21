package cn.edu.zafu.news.net.parser.impl;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zafu.news.net.parser.Parser;
import cn.edu.zafu.news.model.SearchItem;

/**
 * Created by lizhangqu on 2015/5/16.
 */
public class SearchParser implements Parser<List<SearchItem>> {
    @Override
    public List<SearchItem> convert(String from) {
        List<SearchItem> lists = new ArrayList<SearchItem>();
        Document document = Jsoup.parse(from);
        Elements select = document.select("div.content-right ul li");
        String title = null;
        String category = null;

        if("没有数据".equals(select.text())){
            return lists;
        }
        for (Element element : select) {
            SearchItem item = new SearchItem();
            Elements timeElement = element.select("span");
            item.setTime(timeElement.text());
            Elements titleElement = element.select("a");
            title=titleElement.attr("title");
            item.setTitle(title);
            category=titleElement.text();
            Log.d("TAG","asas"+category);
            category=category.substring(1,category.indexOf("]"));
            item.setCategory(category);
            if(titleElement.attr("href").startsWith("/")){
                item.setUrl("http://news.zafu.edu.cn" + titleElement.attr("href"));
            }else{
                item.setUrl(titleElement.attr("href"));
            }
            Log.d("TAG",item.toString());
            lists.add(item);
        }
        return lists;
    }
}
