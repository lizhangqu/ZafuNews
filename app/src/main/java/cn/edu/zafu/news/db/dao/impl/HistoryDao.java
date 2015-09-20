package cn.edu.zafu.news.db.dao.impl;

import android.content.Context;

import cn.edu.zafu.news.db.model.History;

/**
 * User: lizhangqu(513163535@qq.com)
 * Date: 2015-09-20
 * Time: 10:26
 * FIXME
 */
public class HistoryDao extends BaseDaoImpl<History,Integer> {
    public HistoryDao(Context context, Class<History> clazz) {
        super(context, clazz);
    }
}
