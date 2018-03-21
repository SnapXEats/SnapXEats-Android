package com.snapxeats.common.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.common.model.foodGestures.DaoSession;
import com.snapxeats.common.model.location.Location;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 31/1/18.
 */

@Singleton
public class AppUtility {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private DaoSession daoSession;
    private SnapxDataDao snapxDataDao;
    private SnapxData snapxData;

    @Inject
    public AppUtility() {
    }

    public void setContext(Context context) {
        this.mContext = context;
        daoSession = ((SnapXApplication) context.getApplicationContext()).getDaoSession();
        snapxDataDao = daoSession.getSnapxDataDao();
        if (snapxDataDao.loadAll() != null && snapxDataDao.loadAll().size() > 0) {
            snapxData = snapxDataDao.loadAll().get(0);
        }
    }

    public SharedPreferences getSharedPreferences() {
        preferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_name),
                mContext.MODE_PRIVATE);
        return preferences;
    }

    public void saveObjectInPref(Location location, String key) {
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(location);
        editor.putString(key, json);
        editor.apply();
    }

    public String getAuthToken(final Context context) {
        String token = null;
        SnapXApplication app = (SnapXApplication) context.getApplicationContext();
        if (null != app) {
            token = app.getToken();
            if (null != token && !token.isEmpty()) {
                return String.format("Bearer %s", token);
            } else {
                //TODO: fetch it from DB, assign it to app.token & return that token
                if (snapxData != null) {
                    token = snapxData.getToken(); // fetch it from DB}
                    app.setToken(token);
                }
                return String.format("Bearer %s", token);
            }
        }
        return token;
    }

    public void setAuthToken(final Context context) {
        String token = null;
        SnapXApplication app = (SnapXApplication) context.getApplicationContext();
        if (null != app) {
            token = app.getToken();
            if (null != token && !token.isEmpty()) {
                //TODO: fetch it from DB, assign it to app.token & return that token
                if (snapxData != null) {
                    token = snapxData.getToken(); // fetch it from DB
                }
                app.setToken(token);
            }
        }
    }

}
