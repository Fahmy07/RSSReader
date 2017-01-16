package com.afc.android.news.database;

import android.provider.BaseColumns;

/**
 * Created by hp on 1/5/2017.
 */

public class NewsTable {
    public static final String COLUMN_ID = "itemId";
    public static final String NEWS_TABLE = "news";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_PUB_DATE = "pubDate";
    public static final String COLUMN_THUMBNAIL = "thumbnail";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_LINK, COLUMN_TITLE,
            COLUMN_PUB_DATE, COLUMN_THUMBNAIL};

    public static final String CREATE_NEWS_TABLE =
            "CREATE TABLE " + NEWS_TABLE + "(" +
            COLUMN_ID + " TEXT PRIMARY KEY," +
            COLUMN_TITLE + " TEXT," +
            COLUMN_LINK + " TEXT," +
            COLUMN_PUB_DATE + " TEXT," +
            COLUMN_THUMBNAIL + " TEXT" + ");";

    public static final String DELETE_NEWS_TABLE =
            "DROP TABLE" + NEWS_TABLE;
}
