package com.afc.android.news.model;

import android.content.ContentValues;

import com.afc.android.news.database.NewsTable;

import java.util.UUID;

/**
 * Created by hp on 12/30/2016.
 */

public class NewsItem {
    private String mItemId;
    private String mTitle;
    private String mLink;
    //    private String mDescription;
    private String mPubDate;
    private String mThumbnail;
    private String mLogo;

    public NewsItem() {
        if(mItemId == null) {
            mItemId = UUID.randomUUID().toString();
        }
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String itemId) {
        mItemId = itemId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

//    public String getDescription() {
//        return mDescription;
//    }
//
//    public void setDescription(String description) {
//        mDescription = description;
//    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String pubDate) {
        mPubDate = pubDate;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getLogo() {
        return mLogo;
    }

    public void setLogo(String logo) {
        mLogo = logo;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(NewsTable.COLUMN_ID, mItemId);
        values.put(NewsTable.COLUMN_TITLE, mTitle);
        values.put(NewsTable.COLUMN_LINK, mLink);
        values.put(NewsTable.COLUMN_PUB_DATE, mPubDate);
        values.put(NewsTable.COLUMN_THUMBNAIL, mThumbnail);
        return values;
    }
}
