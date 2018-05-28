package com.snapxeats;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.snapxeats.common.model.foodGestures.DaoMaster;
import com.snapxeats.common.model.foodGestures.DaoSession;
import com.snapxeats.dagger.AppComponent;
import com.snapxeats.dagger.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by Prajakta Patil on 28/12/17.
 */

public final class SnapXApplication extends DaggerApplication {
    public DaoSession daoSession;
    private String token;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "SnapXDb");

        /* While upgrading DB */
//        helper.onUpgrade(helper.getWritableDatabase(),603,701);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

