package cn.edu.zafu.news.db.dao.impl;

import android.content.Context;

import cn.edu.zafu.news.db.model.NewsItem;

/**
 * User: lizhangqu(513163535@qq.com)
 * Date: 2015-09-20
 * Time: 10:26
 * FIXME
 */
public class NewsItemDao extends BaseDaoImpl<NewsItem,Integer> {
    public NewsItemDao(Context context, Class<NewsItem> clazz) {
        super(context, clazz);
    }
}
