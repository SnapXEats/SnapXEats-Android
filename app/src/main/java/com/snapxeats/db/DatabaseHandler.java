package com.snapxeats.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.snapxeats.common.model.UserCuisinePreferences;

import java.util.List;

/**
 * Created by Snehal Tembare on 23/2/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SnapXDb";
    private static final String TABLE_USER_INFO = "SnapXUserInfo";

    //Column names
    private static final String SERVER_TOKEN = "serverToken";
    private static final String SERVER_USER_ID = "serverUserId";
    private static final String SOCIAL_TOKEN = "socialToken";
    private static final String SOCIAL_USER_ID = "socialUserId";
    private static final String USER_IMAGE = "userImage";
    private static final String USER_NAME = "userName";
    private static final String SOCIAL_PLATFORM = "socialPlatform";
    private static final boolean IS_FIRST_TIME_USER = false;
    private static final List<UserCuisinePreferences> selectedCuisinesList = null;

    public DatabaseHandler(Context context,
                           String name,
                           SQLiteDatabase.CursorFactory factory,
                           int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE" + TABLE_USER_INFO + "(" +
                SERVER_USER_ID + " text," +
                SERVER_TOKEN + " text," +
                SOCIAL_TOKEN + " text," +
                SOCIAL_USER_ID + " text," +
                USER_IMAGE + " text," +
                USER_NAME + " text," +
                SOCIAL_PLATFORM + " text," +
                IS_FIRST_TIME_USER + " text)";

        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFO);
        onCreate(db);
    }
}
