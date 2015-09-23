package cn.edu.zafu.news.net.parser.impl;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zafu.news.model.NewsContent;
import cn.edu.zafu.news.net.parser.Parser;

/**
 * Created by lizhangqu on 2015/5/16.
 */
public class ContentParser implements Parser<List<NewsContent>> {
    @Override
    public List<NewsContent> convert(String from) {
        List<NewsContent> lists=new ArrayList<NewsContent>();
        NewsContent newsContent=null;
        Document document = Jsoup.parse(from);
        Elements content = document.select("div.content-right");
        Elements title = content.select("h1.title2");
        if("".equals(title.text())){
            title = content.select("h1.title");
        }
        newsContent=new NewsContent(NewsContent.TYPE_TITLE,title.html());
        lists.add(newsContent);
        Elements info = content.select("h1.info");
        newsContent=new NewsContent(NewsContent.TYPE_INFO,info.html());
        lists.add(newsContent);
        Elements param = content.select("div#article_content p");
        for (Element element : param) {
            Log.e("TAG", element + "");
            newsContent=new NewsContent();
            Elements img = element.select("img");
            Elements file = element.select("a");
            boolean isFile=element.text().startsWith("附件：");
            if(img.size()!=0){
                newsContent.setType(NewsContent.TYPE_IMAGE);
                newsContent.setContent("http://news.zafu.edu.cn" + img.attr("src"));
                lists.add(newsContent);
            }else if(isFile&&file.size()!=0){
                newsContent.setType(NewsContent.TYPE_FILE);
                newsContent.setContent("http://news.zafu.edu.cn" + file.attr("href") + "|" + element.text().trim());
                lists.add(newsContent);
                Log.e("TAG 是附件", newsContent + "");
            }else{
                newsContent.setType(NewsContent.TYPE_PARAM);
                newsContent.setContent(element.html());
                lists.add(newsContent);

            }
        }

        return lists;
    }
}
