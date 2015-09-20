package cn.edu.zafu.news.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import cn.edu.zafu.news.db.dao.BaseDao;
import cn.edu.zafu.news.db.dao.impl.HistoryDao;
import cn.edu.zafu.news.db.dao.impl.NewsItemDao;
import cn.edu.zafu.news.db.model.History;
import cn.edu.zafu.news.db.model.NewsItem;

/**
 * Helper类，提供单例Helper
 * User:lizhangqu(513163535@qq.com)
 * Date:2015-08-26
 * Time: 12:04
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "zafu_news.db";
    //数据库名
    private static final int DB_VERSION = 1;
    //数据库版本
    private static DatabaseHelper instance;

    //Helper单例
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        //创建表
        try {
            TableUtils.createTable(connectionSource, History.class);
            TableUtils.createTable(connectionSource, NewsItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //更新表
        try {
            TableUtils.dropTable(connectionSource, History.class, true);
            TableUtils.dropTable(connectionSource, NewsItem.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 双重加锁检查
     *
     * @param context 上下文
     * @return 单例
     */
    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    private static Object mHistoryObject = new Object();
    private static BaseDao<History, Integer> mHistoryDao;

    public static BaseDao<History, Integer> getHistoryDao(Context context) {
        if (mHistoryDao == null) {
            synchronized (mHistoryObject) {
                if (mHistoryDao == null) {
                    mHistoryDao = new HistoryDao(context, History.class);
                }
            }

        }
        return mHistoryDao;
    }

    private static Object mNewsItemObject = new Object();
    private static BaseDao<NewsItem, Integer> mNewsItemDao;

    public static BaseDao<NewsItem, Integer> getNewsItemDao(Context context) {
        if (mNewsItemDao == null) {
            synchronized (mNewsItemObject) {
                if (mNewsItemDao == null) {
                    mNewsItemDao = new NewsItemDao(context, NewsItem.class);
                }
            }
        }
        return mNewsItemDao;
    }
}