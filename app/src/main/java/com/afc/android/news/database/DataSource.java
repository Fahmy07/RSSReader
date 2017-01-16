package com.afc.android.news.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.afc.android.news.model.NewsItem;

import java.util.ArrayList;

/**
 * Created by hp on 1/7/2017.
 */

public class DataSource {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private SQLiteOpenHelper mDBHelper;
    private Cursor mCursor;

    public DataSource(Context context) {
        mContext = context;
        mDBHelper = new DBHelper(mContext);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void close() {
        mDBHelper.close();
    }

    public void createNews(NewsItem newsItem) {
        ContentValues values = newsItem.toValues();
        // Check if row already existed in database
        if (!isLinkExists(mDatabase, newsItem.getLink())) {
            // news item not existed, create a new row
            mDatabase.insert(NewsTable.NEWS_TABLE, null, values);
        } else {
            // news item already existed update the row
            updateLink(newsItem);
        }
    }

    public ArrayList<NewsItem> getAllNews() {
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        mCursor = mDatabase.query(NewsTable.NEWS_TABLE, NewsTable.ALL_COLUMNS,
                null, null, null, null, NewsTable.COLUMN_PUB_DATE + " DESC");
        while (mCursor.moveToNext()) {
            NewsItem newsItem = new NewsItem();
            newsItem.setTitle(mCursor.getString(mCursor.getColumnIndex(NewsTable.COLUMN_TITLE)));
            newsItem.setLink(mCursor.getString(mCursor.getColumnIndex(NewsTable.COLUMN_LINK)));
            newsItem.setPubDate(mCursor.getString(mCursor.getColumnIndex(NewsTable.COLUMN_PUB_DATE)));
            newsItem.setThumbnail(mCursor.getString(mCursor.getColumnIndex(NewsTable.COLUMN_THUMBNAIL)));
            newsItems.add(newsItem);
        }
        return newsItems;
    }

    /**
     * Updating a single row will be identified by rss link
     */
    public int updateLink(NewsItem item) {

        ContentValues values = item.toValues();

        // updating row return
        return mDatabase.update(NewsTable.NEWS_TABLE, values, NewsTable.COLUMN_LINK + " = ?",
                new String[]{String.valueOf(item.getLink())});

    }

    /**
     * Deleting single row
     */
//    public void deleteLink(NewsItem item) {
//        mDatabase.delete(NewsTable.NEWS_TABLE, NewsTable.COLUMN_ID + " = ?",
//                new String[]{String.valueOf(item.getItemId())});
//    }

    /**
     * Checking whether a news item is already existed check is done by matching rss
     * link
     */
    public boolean isLinkExists(SQLiteDatabase db, String link) {

        mCursor = db.rawQuery("SELECT 1 FROM " + NewsTable.NEWS_TABLE
                + " WHERE link = '" + link + "'", new String[]{});
        boolean exists = (mCursor.getCount() > 0);
        return exists;
    }
}
