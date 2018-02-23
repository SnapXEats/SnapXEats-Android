package com.snapxeats.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Snehal Tembare on 23/2/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SnapXDb";
    private static final String TABLE_USER_INFO = "SnapXUserInfo";

    //Column names
    private static final String TOKEN = "serverToken";
    private static final String USER_ID = "serverUserId";
    private static final String INSTA_TOKEN = "instagramToken";
    private static final String INSTA_USER_ID = "instagramUserId";
    private static final String INSTA_USER_IMAGE = "instagramUserImage";
    private static final String INSTA_USER_NAME = "instagramUserName";
    private static final String SOCIAL_PLATFORM = "socialPlatform";
    private static final boolean IS_FIRST_TIME_USER = false;

    public DatabaseHandler(Context context,
                           String name,
                           SQLiteDatabase.CursorFactory factory,
                           int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
